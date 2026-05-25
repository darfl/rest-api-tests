package tests;

import static TestData.TestData.MISSING_EMAIL_MESSAGE;
import static TestData.TestData.MISSING_PASSWORD_MESSAGE;
import static TestData.TestData.USER_NOT_FOUND;
import static TestData.TestData.sendLoginRequest;
import static TestData.TestData.sendLoginWithInvalidEmailRequest;
import static TestData.TestData.sendLoginWithInvalidPasswordRequest;
import static TestData.TestData.sendLoginWithoutPasswordRequest;
import static TestData.TestData.sendPasswordWithoutEmailRequest;
import static endpoints.Endpoints.LOGIN;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static specs.RequestSpecs.requestSpecification;
import static specs.ResponseSpecs.responseSpec;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import models.SuccessfulLoginResponse;
import models.UnsuccessfulLoginResponse;

public class LoginTests extends TestBase {

    @Test
    @DisplayName("Проверка успешной авторизации с валидным логином и паролем c возвращением заполненного значения token" )
    void successfulAuthWithNotNullValueTokenTest() {
        SuccessfulLoginResponse response =
                step("Отправка запроса на авторизацию с валидным логинм и паролем", () ->
                        given(requestSpecification)
                                .body(sendLoginRequest())
                        .when()
                                .post(LOGIN)
                        .then()
                                .spec(responseSpec(200))
                                .extract().as(SuccessfulLoginResponse.class));
        step("Проверка ответа о возвращении заполненного значения token", () ->
                assertThat(response.getToken(), is(notNullValue())));
    }
    @Test
    @DisplayName("Проверка наличия текста _user not found_ при неуспешной авторизации при введении невалидного пароля ")
    void userNotFoundInvalidPasswordTest() {
        UnsuccessfulLoginResponse response =
                step("Отправка запроса на авторизацию с невалидным паролем", () ->
                        given(requestSpecification)
                                .body(sendLoginWithInvalidPasswordRequest())
                        .when()
                                .post(LOGIN)
                        .then()
                                .spec(responseSpec(400))
                                .extract().as(UnsuccessfulLoginResponse.class));
        step("Проверка ответа об отсутсвии найденного пользователя", () ->
                assertThat(response.getError(), is(USER_NOT_FOUND)));
    }

    @Test
    @DisplayName("Проверка наличия текста _user not found_ при неуспешной авторизации при введении невалидного логина ")
    void userNotFoundInvalidLoginTest() {
        UnsuccessfulLoginResponse response =
                step("Отправка запроса на авторизацию с невалидным логином", () ->
                        given(requestSpecification)
                                .body(sendLoginWithInvalidEmailRequest())
                        .when()
                                .post(LOGIN)
                        .then()
                                .spec(responseSpec(400))
                                .extract().as(UnsuccessfulLoginResponse.class));
        step("Проверка ответа об отсутсвии найденного пользователя", () ->
                assertThat(response.getError(), is(USER_NOT_FOUND)));
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
                                .body(sendPasswordWithoutEmailRequest())
                        .when()
                                .post(LOGIN)
                        .then()
                                .spec(responseSpec(400))
                                .extract().as(UnsuccessfulLoginResponse.class));
        step("Проверка ответа об отсутсвии введенного логина", () ->
                assertThat(response.getError(), is(MISSING_EMAIL_MESSAGE)));
    }
}