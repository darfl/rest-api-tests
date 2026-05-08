package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import models.lombok.LoginBodyLombokModel;
import models.lombok.LoginResponseLombokModel;
import models.pojo.LoginBodyModel;
import models.pojo.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginTestsWithTestBase extends TestBase {

    @Test
    @DisplayName("Проверка успешной авторизации с валидным логином и паролем" )
    void successfulLoginPojoTest() {

        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseModel response = given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .log().body()
                .log().headers()
        .when()
                .post("/login")

        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseModel.class);
        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
    }

    @Test
    @DisplayName("Проверка успешной авторизации с валидным логином и паролем" )
    void successfulLoginLombokAllureTest() {

        LoginBodyLombokModel  authData = new LoginBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseLombokModel response = given()
                .filter(new AllureRestAssured())
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .log().body()
                .log().headers()
        .when()
                .post("/login")

        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseLombokModel.class);
        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
    }

    @Test
    @DisplayName("Проверка успешной авторизации с валидным логином и паролем" )
    void successfulLoginLombokCustomAllureTest() {

        LoginBodyLombokModel  authData = new LoginBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseLombokModel response = step("Make request", ()-> given()
                 .filter(withCustomTemplates())
                 .body(authData)
                 .contentType(JSON)
                 .log().uri()
                 .log().body()
                 .log().headers()
        .when()
                 .post("/login")

        .then()
                  .log().status()
                  .log().body()
                  .statusCode(200)
                  .extract().as(LoginResponseLombokModel.class));

        step("Check response", ()->
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken()));
    }

    @Test
    @DisplayName("Проверка успешной авторизации с валидным логином и паролем c возвращением заполненного значения token" )
    void successfulAuthWithNotNullValueTokenTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .log().body()
                .log().headers()
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
                .log().uri()
                .log().body()
                .log().headers()
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
