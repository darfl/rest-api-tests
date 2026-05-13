package tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static TestData.TestData.UPDATE_USER_PATH;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UpdateUsersTests extends TestBase {
    @Test
    @DisplayName("Проверка обновления имени и должности пользователя")
    void updateNameAndJobTest() {
        String name = "morpheus";
        String job = "zion resident";
        String requestBody = String.format("""
            {
                "name": "%s",
                "job": "%s"
            }
            """, name, job);
        given()
                .log().uri()
                .contentType(ContentType.JSON)
                .body(requestBody)
        .when()
                .put(UPDATE_USER_PATH)
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .assertThat()
                .body("name", equalTo(name))
                .body("job", equalTo(job));
    }
}
