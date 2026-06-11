# REST API Test Suite для Reqres.in

> **Автоматизированное тестирование REST API с человеческим лицом: читаемые отчёты Allure, переиспользуемые спецификации и ни одного захардкоженного значения в теле теста.**

---

## Проблема → Решение

**Проблема:** ручное тестирование каждого эндпоинта REST API — медленно, невоспроизводимо и не оставляет артефактов для отладки. Писать тесты «в лоб» — значит копипастить `given().header("x-api-key", "…").contentType(JSON)` в каждом тесте.

**Решение:** слоистая архитектура на JUnit 5 + RestAssured + Allure, где:
- **Спецификации** (RequestSpecs / ResponseSpecs) вынесены в отдельный слой — код запросов и ответов описан **один раз**;
- **Модели** (Lombok POJO) автоматически мапят JSON в типизированные объекты — никаких `jsonPath().getString()` в тестах;
- **Allure-отчёты** с кастомными HTML-шаблонами FreeMarker генерируются **после каждого прогона** и показывают полную картину: request, response, headers, curl.

---

## Стек технологий

| Категория | Инструмент | Версия |
|-----------|-----------|--------|
| Язык | Java | 17+ |
| HTTP-клиент | RestAssured | 5.5.6 |
| Тестовый фреймворк | JUnit Jupiter | 5.10.0 |
| Отчётность | Allure Framework | 2.27.0 |
| Генерация кода | Lombok (freefair plugin) | 9.5.0 |
| Логирование | SLF4J Simple | 2.0.7 |
| Валидация схем | RestAssured json-schema-validator | 5.5.6 |
| Сборка | Gradle (wrapper) | — |

---

## Ключевые паттерны в коде

### 1. Слоевая архитектура (Separation of Concerns)

```
src/test/java/
├── endpoints/Endpoints.java       ← строковые константы URL
├── models/                        ← Lombok @Data POJO + @JsonIgnoreProperties
├── specs/RequestSpecs.java        ← RequestSpecification (ключ, шаблоны, логи)
├── specs/ResponseSpecs.java       ← ResponseSpecification (статус, логи)
├── TestData/TestData.java         ← фабрики тестовых данных + константы
├── helpers/CustomAllureListener.java ← кастомные FreeMarker-шаблоны
└── tests/                         ← сами тесты (расширяют TestBase)
```

Тестовый класс **не содержит** ни URL, ни заголовков, ни логики конструирования тела запроса — только вызов `given(requestSpecification).body(sendLoginRequest()).when().post(LOGIN)`.

### 2. DRY через ResponseSpecBuilder

Вместо повторения `.then().statusCode(200).log().all()` в каждом тесте — статические константы:

```java
public static final ResponseSpecification status200 = responseSpec(200);
// …
public static ResponseSpecification responseSpec(int statusCode) {
    return new ResponseSpecBuilder()
            .expectStatusCode(statusCode)
            .log(STATUS).log(BODY)
            .build();
}
```

### 3. Шаблонный метод TestBase

```java
public class TestBase {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }
}
```

Все тестовые классы расширяют `TestBase` — базовая конфигурация задаётся **до запуска первого теста**, а не дублируется в каждом классе.

### 4. Фабрики тестовых данных

```java
public static LoginRequest sendLoginRequest() { … }
public static LoginRequest sendLoginWithInvalidPasswordRequest() { … }
public static LoginRequest sendLoginWithoutPasswordRequest() { … }
```

Каждый метод-фабрика инкапсулирует создание тела запроса для конкретного сценария. Тест только вызывает фабрику — данные всегда консистентны, а их изменение требует правки в одном месте.

---

## Быстрый старт

```bash
# 1. Клонировать репозиторий
git clone https://github.com/darfl/rest-api-tests.git && cd rest-api-tests

# 2. Установить переменную окружения (API-ключ для reqres.in)
export REQRES_API_KEY="ваш_ключ"

# 3. Запустить все тесты
./gradlew test

# 4. Сгенерировать и открыть Allure-отчёт
./gradlew allureServe

# 5. Запустить конкретный тестовый класс
./gradlew test --tests "tests.LoginTests"

# 6. Запустить конкретный тест-метод
./gradlew test --tests "tests.LoginTests.successfulAuthWithNotNullValueTokenTest"
```

---

## Что на самом деле внутри

### Allure: не просто «красивые отчёты», а кастомные HTML-шаблоны

В проекте используются **собственные FreeMarker-шаблоны** (`src/test/resources/tpl/request.ftl`, `response.ftl`) с подсветкой синтаксиса Highlight.js, Bootstrap-вёрсткой и выводом curl-команды. Каждый HTTP-обмен в отчёте — это полноценная HTML-страница:

- **Метод и URL**
- **Body** (с подсветкой JSON)
- **Headers** (все заголовки запроса/ответа)
- **Cookies**
- **Curl-команда** для воспроизведения запроса из терминала

Шаблон подключается через кастомный `AllureRestAssured`-фильтр в `CustomAllureListener`.

### Allure.step: читаемые шаги в отчёте на русском языке

Каждый тест обёрнут в `Allure.step("Проверка …", () -> { … })` — в Allure-отчёте видна не каша из HTTP-вызовов, а пошаговый сценарий:

```
✔ Отправка запроса на авторизацию с валидным логином и паролем
  └─ POST https://reqres.in/api/login → 200
✔ Проверка ответа о возвращении заполненного значения token
  └─ assertThat(response.getToken(), is(notNullValue()))
```

### Десериализация через Lombok POJO

Все ответы десериализуются в типизированные объекты:

```java
.extract().as(SuccessfulLoginResponse.class)
```

Модели помечены `@JsonIgnoreProperties(ignoreUnknown = true)` — новые поля в API не ломают тесты.

---

## «Скрытые компетенции» — что видно между строк

1. **Обработка граничных случаев.** 5 тестов только на логин: валидный кейс, невалидный пароль, невалидный email, отсутствие пароля, отсутствие email.
2. **Кастомные отчёты Allure.** Не дефолтные вложения, а собственные FreeMarker-шаблоны с подсветкой кода и curl-сниппетом — существенно ускоряет отладку упавших тестов.
3. **Логирование каждого запроса и ответа.** `.log().uri().log().body().log().headers()` в RequestSpecs + `.log(STATUS).log(BODY)` в ResponseSpecs — консольный вывод полный и структурированный.
4. **Gradle Wrapper.** `./gradlew` гарантирует, что любой разработчик запустит тесты той же версией Gradle без установки Gradle в систему.
5. **Lombok-плагин.** Не просто `compileOnly`, а через Gradle-плагин `io.freefair.lombok` — annotation processor работает прозрачно.
6. **AspectJ Weaving для Allure.** Включён в build.gradle — Allure автоматически перехватывает HTTP-обмены без ручного добавления фильтров в каждый тест.
7. **json-schema-validator в зависимостях.** Библиотека подключена (хоть и не используется напрямую в текущих тестах) — задел на контрактное тестирование.

---

## Что можно улучшить (демонстрирует инженерное мышление)

| Улучшение | Зачем |
|-----------|-------|
| **Параметризованные тесты** `@ParameterizedTest` | Вместо 3 отдельных тестов на невалидный логин/пароль/email — один параметризованный с `@CsvSource` или `@MethodSource`. Меньше кода, выше покрытие. |
| **Конфигурация через .properties / .env** | `baseURI`, `basePath` и `API_KEY` читать из `test.properties` с fallback на переменные окружения — тесты можно гонять на разных стендах без перекомпиляции. |
| **Schema Validation** | Библиотека `json-schema-validator` уже в зависимостях — добавить `.body(matchesJsonSchemaInClasspath("schemas/user.json"))` для проверки контракта API. |
| **Параллельный запуск** | Включить `junit-platform.properties` с `junit.jupiter.execution.parallel.enabled=true` — на больших сьютах сокращает время прогона в разы. |
| **CI-пайплайн** | Добавить `.github/workflows/test.yml` — запуск тестов + публикация Allure-отчёта на GitHub Pages при каждом пуше. |
| **TestNG как альтернатива** | Для более сложной оркестрации (зависимости между тестами, группировка) рассмотреть переход на TestNG. |
| **Исправить опечатки в @DisplayName** | «логинм» → «логином», «отсутсвии» → «отсутствии», «отсутсвии» → «отсутствии». Мелочь, но видна внимательность к деталям. |
| **Вынести значения в TestData из тела тестов** | `EMAIL` проверяется в `GetResourcesTests`, но жёстко завязан на порядковый индекс `.get(0)` — если API изменит порядок, тест упадёт. Лучше искать по id или проверять contains. |

---

## Структура проекта

```
.
├── build.gradle                          ← зависимости, плагины, конфигурация Allure
├── gradlew / gradlew.bat                 ← Gradle Wrapper
├── gradle/wrapper/                       ← JAR и properties для wrapper'а
├── CLAUDE.md                             ← памятка для AI-ассистента
└── src/test/
    ├── java/
    │   ├── endpoints/Endpoints.java      ← URL-константы
    │   ├── helpers/CustomAllureListener.java ← кастомные шаблоны Allure
    │   ├── models/                       ← Lombok-модели запросов/ответов
    │   │   ├── Colour.java
    │   │   ├── ColourResponse.java
    │   │   ├── LoginRequest.java
    │   │   ├── SuccessfulLoginResponse.java
    │   │   ├── Support.java
    │   │   ├── UnsuccessfulLoginResponse.java
    │   │   ├── UpdateDataRequest.java
    │   │   ├── UpdateDataResponse.java
    │   │   ├── User.java
    │   │   └── UserResponse.java
    │   ├── specs/
    │   │   ├── RequestSpecs.java         ← переиспользуемая спецификация запросов
    │   │   └── ResponseSpecs.java        ← переиспользуемая спецификация ответов
    │   ├── TestData/TestData.java        ← константы + фабрики тестовых данных
    │   └── tests/
    │       ├── TestBase.java             ← @BeforeAll: baseURI + basePath
    │       ├── LoginTests.java           ← 5 тестов авторизации
    │       ├── GetResourcesTests.java    ← 4 теста получения ресурсов
    │       ├── UpdateUsersTests.java     ← тест обновления пользователя
    │       └── DeleteTests.java          ← тест удаления пользователя
    └── resources/
        └── tpl/
            ├── request.ftl               ← кастомный HTML-шаблон запроса
            └── response.ftl              ← кастомный HTML-шаблон ответа
```

---

## Пример работы

### Тест: успешная авторизация

```java
@Test
@DisplayName("Проверка успешной авторизации с валидным логином и паролем c возвращением заполненного значения token")
void successfulAuthWithNotNullValueTokenTest() {
    SuccessfulLoginResponse response =
            step("Отправка запроса на авторизацию с валидным логином и паролем", () ->
                    given(requestSpecification)
                            .body(sendLoginRequest())
                    .when()
                            .post(LOGIN)
                    .then()
                            .spec(responseSpec(200))
                            .extract().as(SuccessfulLoginResponse.class));
    step("Проверка ответа о возвращении заполненного значения token", () ->
            assertThat(response.getToken(), is(notNullValue())));
}
```

### Консольный вывод

```
Request method: POST
Request URI:    https://reqres.in/api/login
Content-Type:   application/json
Headers:        x-api-key=***
Body:
{
    "email": "eve.holt@reqres.in",
    "password": "cityslicka"
}
HTTP/1.1 200 OK
Body:
{
    "token": "QpwL5tke4Pnpja7X4"
}
```

### Allure-отчёт

Запрос и ответ отображаются в кастомных HTML-шаблонах с подсветкой синтаксиса, списком всех заголовков и curl-командой для повторного воспроизведения.

---

<p align="center">
  <b>Built with RestAssured · JUnit 5 · Allure · Lombok · Gradle</b>
</p>