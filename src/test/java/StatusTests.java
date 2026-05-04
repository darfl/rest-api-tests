import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

public class StatusTests {

    @Test
    void checkTotal5() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .log().all()
                .body("total", is(5));
    }


    @Test
    void checkTotalWithResponseLogs() {
        // 1. Сохраняем ответ в переменную
        Response response = get("https://selenoid.autotests.cloud/status");
        // 2. Извлекаем значение total из ответа
        int actualTotal = response.then().extract().path("total");
        // 3. Проверяем, что total > 0 (сервер жив и может принимать хотя бы 1 сессию)
        response.then().log().all()
                .body("total", greaterThan(0)); // 'greaterThan' - другой матчер
    }
}