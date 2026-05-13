package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static TestData.TestData.UPDATE_USER_PATH;
import static io.restassured.RestAssured.given;
import static specs.ResponseSpecs.responseSpec;

public class DeleteTests extends TestBase {

    @Test
    @DisplayName("Проверка удаления пользователя" )
    void deleteResourcesTest() {
        given()
                .log().uri()
                .log().headers()
        .when()
                .delete(UPDATE_USER_PATH)

        .then()
                .log().status()
                .log().body()
                .spec(responseSpec(204));
    }
}
