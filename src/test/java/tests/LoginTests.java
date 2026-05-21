package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import models.UnsuccessfulLoginResponse;
import models.lombok.LoginBodyLombokModel;
import models.lombok.LoginResponseLombokModel;
import models.pojo.LoginBodyModel;
import models.pojo.LoginResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static TestData.TestData.*;
import static endpoints.Endpoints.LOGIN;
import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.LoginSpec.loginRequestSpec;
import static specs.LoginSpec.loginResponseSpec;
import static specs.RequestSpecs.requestSpecification;
import static specs.ResponseSpecs.responseSpec;

public class LoginTests extends TestBase {

    /*@Test
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
                .post(LOGIN)

        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseModel.class);
        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
    }*/

    /*@Test
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
                .post(LOGIN)

        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseLombokModel.class);
        assertEquals("QpwL5tke4Pnpja7X4", response.getToken());
    }*/

    /*@Test
    @DisplayName("Проверка успешной авторизации с валидным логином и паролем" )
    void successfulLoginLombokCustomAllureWithStepsTest() {

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
                 .post(LOGIN)

        .then()
                  .log().status()
                  .log().body()
                  .statusCode(200)
                  .extract().as(LoginResponseLombokModel.class));

        step("Check response", ()->
            assertEquals("QpwL5tke4Pnpja7X4", response.getToken()));
    }*/

    /*@Test
    @DisplayName("Проверка успешной авторизации с валидным логином и паролем" )
    void successfulLoginLombokWithSpecsTest() {

        LoginBodyLombokModel  authData = new LoginBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseLombokModel response = step("Make request", ()->
             given(loginRequestSpec)
                .body(authData)

            .when()
                .post(LOGIN)

            .then()
                .spec(loginResponseSpec)
                .extract().as(LoginResponseLombokModel.class));

        step("Check response", ()->
                assertEquals("QpwL5tke4Pnpja7X4", response.getToken()));
    }*/
    @Test
    @DisplayName("Проверка успешной авторизации с валидным логином и паролем" )
    void successfulLoginLombokWithSpecsTest() {

        LoginBodyLombokModel  authData = new LoginBodyLombokModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

        LoginResponseLombokModel response = step("Make request", ()->
                given(loginRequestSpec)
                        .body(authData)

                .when()
                        .post(LOGIN)

                .then()
                        .spec(loginResponseSpec)
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
                .post(LOGIN)

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
                .post(LOGIN)

        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is(USER_NOT_FOUND));
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
                .post(LOGIN)

        .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is(USER_NOT_FOUND));
    }
    @Test
    @DisplayName("Проверка наличия текста _Missing password_ при неуспешной авторизации при отсутствии введенного пароля ")
    void missingPasswordInvalidLoginTest() {
        UnsuccessfulLoginResponse response =
                step("Отправка запроса на авторизацию без пароля", () ->
        given(requestSpecification)
                .body(sendLoginWithoutPasswordRequest())
        .when()
                .post(LOGIN)
        .then()
                .spec(responseSpec(400))
                .extract().as(UnsuccessfulLoginResponse.class));
        step("Проверка ответа об отсутсвии введенного пароля", () ->
                assertThat(response.getError(), is(MISSING_PASSWORD_MESSAGE)));
    }

    @Test
    @DisplayName("Проверка наличия текста _Missing password_ при неуспешной авторизации при отсутствии введенного логина ")
    void missingEmailInvalidLoginTest() {
        UnsuccessfulLoginResponse response =
                step("Отправка запроса на авторизацию без логина", () ->
                        given(requestSpecification)
                                .body(sendPasswordWithoutLoginRequest())
                        .when()
                                .post(LOGIN)
                        .then()
                                .spec(responseSpec(400))
                                .extract().as(UnsuccessfulLoginResponse.class));
        step("Проверка ответа об отсутсвии введенного логина", () ->
                assertThat(response.getError(), is(MISSING_EMAIL_MESSAGE)));
    }
}

