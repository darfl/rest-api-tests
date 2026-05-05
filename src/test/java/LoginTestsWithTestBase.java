import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTestsWithTestBase extends TestBase {

    @Test
    @DisplayName("Проверка успешной авторизации с валидным логином и паролем" )
    void successfulLoginTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";
        given()
                .body(authData)
                .contentType(JSON)
                .log().all()
        .when()
                .post("/login")

        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }
    @Test
    @DisplayName("Проверка успешной авторизации с валидным логином и паролем c возвращением заполненного значения token" )
    void successfulAuthWithNotNullValueTokenTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";
        given()
                .body(authData)
                .contentType(JSON)
                .log().all()
        .when()
                .post("/login")

        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", notNullValue());
    }
    @Test
    @DisplayName("Проверка наличия текста _user not found_ при неуспешной авторизации при введении невалидного пароля ")
    void userNotFoundInvalidPasswordTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"abc\"}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().all()

        .when()
                .post("/login")

        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("user not found"));
    }

    @Test
    @DisplayName("Проверка наличия текста _user not found_ при неуспешной авторизации при введении невалидного логина ")
    void userNotFoundInvalidLoginTest() {
        String authData = "{\"email\": \"eveabc.holt@reqres.in\", \"password\": \"cityslicka\"}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().all()

        .when()
                .post("/login")

        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("user not found"));
    }
    @Test
    @DisplayName("Проверка наличия текста _Missing password_ при неуспешной авторизации при отсутствии введенного пароля ")
    void missingPasswordInvalidLoginTest() {
        String authData = "{\"email\": \"eveabc.holt@reqres.in\", \"password\": \"\"}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().all()

        .when()
                .post("/login")

        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    @DisplayName("Проверка наличия текста _Missing password_ при неуспешной авторизации при отсутствии введенного логина ")
    void missingEmailInvalidLoginTest() {
        String authData = "{\"email\": \"\", \"password\": \"cityslicka\"}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().all()

        .when()
                .post("/login")

        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing email or username"));
    }
}
