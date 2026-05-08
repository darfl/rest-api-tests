package tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UpdateUsersTests extends TestBase {
    @Test
    @DisplayName("Обновить имя и должность пользователя")
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
                .put("/users/2")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .assertThat()
                .body("name", equalTo(name))
                .body("job", equalTo(job));
    }
}
