package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static TestData.TestData.UPDATE_USER_PATH;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static specs.RequestSpecs.requestSpecification;
import static specs.ResponseSpecs.responseSpec;

public class DeleteTests extends TestBase {

    @Test
    @DisplayName("Проверка удаления пользователя" )
    void deleteResourcesTest() {
        step("Отправка запроса на удаление данных пользователя", () ->
            given(requestSpecification)
            .when()
                    .delete(UPDATE_USER_PATH)
            .then()
                    .spec(responseSpec(204)));
    }
}
