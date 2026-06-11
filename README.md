# REST API Test Suite для Reqres.in

Автоматизированное тестирование REST API [Reqres.in](https://reqres.in) на Java. Слоевая архитектура: переиспользуемые спецификации запросов/ответов, Lombok POJO для десериализации, кастомные Allure-отчёты.

## Стек

| Инструмент | Назначение |
|------------|------------|
| Java 17+ | Язык |
| JUnit Jupiter 5.10 | Тестовый фреймворк |
| RestAssured 5.5.6 | HTTP-клиент |
| Allure 2.27 | Отчёты |
| Lombok (freefair plugin 9.5) | POJO-модели |
| SLF4J Simple 2.0.7 | Логирование |
| Gradle (wrapper) | Сборка |

## Быстрый старт

```bash
# 1. Клонировать репозиторий
git clone https://github.com/darfl/rest-api-tests.git && cd rest-api-tests

# 2. Установить API-ключ (опционально)
export REQRES_API_KEY="ваш_ключ"

# 3. Запустить все тесты
./gradlew test

# 4. Сгенерировать и открыть Allure-отчёт
./gradlew allureServe

# 5. Запустить конкретный тестовый класс или метод
./gradlew test --tests "tests.LoginTests"
./gradlew test --tests "tests.LoginTests.successfulAuthWithNotNullValueTokenTest"
```

## Структура проекта

```
.
├── build.gradle
├── gradlew / gradlew.bat
├── gradle/wrapper/
└── src/test/
    ├── java/
    │   ├── endpoints/Endpoints.java            # Строковые константы URL
    │   ├── helpers/CustomAllureListener.java   # Кастомные HTML-шаблоны Allure
    │   ├── models/                             # Lombok POJO запросов/ответов
    │   ├── specs/
    │   │   ├── RequestSpecs.java               # Переиспользуемая спецификация запросов
    │   │   └── ResponseSpecs.java              # Переиспользуемая спецификация ответов
    │   ├── TestData/TestData.java              # Константы и фабрики тестовых данных
    │   └── tests/
    │       ├── TestBase.java                   # @BeforeAll: baseURI + basePath
    │       ├── LoginTests.java                 # 5 тестов авторизации
    │       ├── GetResourcesTests.java          # 4 теста получения ресурсов
    │       ├── UpdateUsersTests.java           # Тест обновления пользователя
    │       └── DeleteTests.java                # Тест удаления пользователя
    └── resources/
        └── tpl/
            ├── request.ftl                     # HTML-шаблон запроса
            └── response.ftl                    # HTML-шаблон ответа
```

## Архитектура

### Спецификации (RequestSpecs / ResponseSpecs)

Конфигурация запросов и ответов вынесена в статические константы — тесты не содержат URL, заголовков и логики логирования:

```java
// RequestSpecs — единая точка настройки запросов: baseURI, headers, logging
public static RequestSpecification requestSpecification = ...

// ResponseSpecs — переиспользуемые проверки статуса
public static final ResponseSpecification status200 = responseSpec(200);

public static ResponseSpecification responseSpec(int statusCode) {
    return new ResponseSpecBuilder()
            .expectStatusCode(statusCode)
            .log(STATUS).log(BODY)
            .build();
}
```

### Модели (Lombok POJO)

Ответы десериализуются в типизированные объекты — никаких `jsonPath().getString()`:

```java
.extract().as(SuccessfulLoginResponse.class)
```

Все модели помечены `@JsonIgnoreProperties(ignoreUnknown = true)` — новые поля в API не ломают тесты.

### Фабрики тестовых данных

Создание тел запросов инкапсулировано в методы `TestData` — тест только вызывает нужную фабрику:

```java
public static LoginRequest sendLoginRequest() { ... }
public static LoginRequest sendLoginWithInvalidPasswordRequest() { ... }
public static LoginRequest sendLoginWithoutPasswordRequest() { ... }
```

### TestBase

Все тестовые классы расширяют `TestBase` — `baseURI` и `basePath` задаются один раз в `@BeforeAll`:

```java
public class TestBase {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }
}
```

### Allure-отчёты

Кастомные FreeMarker-шаблоны (`src/test/resources/tpl/`) формируют HTML-отчёт для каждого HTTP-обмена: метод, URL, body с подсветкой JSON, заголовки, cookies и curl-команда для воспроизведения.

Тесты используют `Allure.step()` — в отчёте виден пошаговый сценарий, а не каша из HTTP-вызовов:

```
✔ Отправка запроса на авторизацию с валидным логином и паролем
  └─ POST https://reqres.in/api/login → 200
✔ Проверка, что token не пустой
  └─ assertThat(response.getToken(), is(notNullValue()))
```

## Пример теста

```java
@Test
@DisplayName("Проверка успешной авторизации с валидным логином и паролем")
void successfulAuthWithNotNullValueTokenTest() {
    SuccessfulLoginResponse response =
            step("Отправка запроса на авторизацию", () ->
                    given(requestSpecification)
                            .body(sendLoginRequest())
                    .when()
                            .post(LOGIN)
                    .then()
                            .spec(responseSpec(200))
                            .extract().as(SuccessfulLoginResponse.class));
    step("Проверка, что token не пустой", () ->
            assertThat(response.getToken(), is(notNullValue())));
}
```

**Консольный вывод:**

```
Request method: POST
Request URI:    https://reqres.in/api/login
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