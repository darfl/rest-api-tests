import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ExampleLoginTests {

    @Test
    void successfulLoginTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";
        given()
                .header("x-api-key", "reqres_3b54ed9fda9845858b4d32e673518693")
                .body(authData)
                .contentType(JSON)
                .log().uri()
        .when()
                .post("https://reqres.in/api/login")

        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));

    }
}
