package tests;

import io.restassured.response.Response;
import models.ColourResponse;
import models.UserResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static TestData.TestData.*;
import static endpoints.Endpoints.UNKNOWN;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.RequestSpecs.requestSpecification;
import static specs.ResponseSpecs.responseSpec;

public class GetResourcesTests extends TestBase {

    @Test
    @DisplayName("Получить n-ую страницу списка пользователей" )
    void getUsersListTest() {
    UserResponse response =
            step("Отправка запроса на получение списка пользователей", () ->
        given(requestSpecification)
                .get(USERS_PAGE_PATH)
        .then()
                .spec(responseSpec(200))
                .extract().as(UserResponse.class));
    step("Проверка 1-го в списке email", () ->
                assertThat(response.getData().get(0).getEmail(), is(EMAIL)));
    }

    @Test
    @DisplayName("Получить список цветов" )
    void getNumOfResourcesTest() {
    ColourResponse response =
                step("Отправка запроса на получение списка цветов", () ->
        given(requestSpecification)
                .get(UNKNOWN)
        .then()
                .spec(responseSpec(200))
                .extract().as(ColourResponse.class));
        step("Проверка названия первого цвета", () ->
                assertThat(response.getData().get(0).getName(), is(COLOUR_NAME)));
        step("Проверка ссылки support", () ->
                assertThat(response.getSupport().getUrl(), is(SUPPORT_URL)));
    }

    @Test
    @DisplayName("Получить количество цветов(размер массива) на странице")
    void getNumOfResourcesOnPageTest() {
        int numberOfResourcesOnPage= 6;
        given()
                .log().uri()
                .log().headers()
        .when()
                .get(UNKNOWN)
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data", hasSize(numberOfResourcesOnPage));
    }

    @Test
    @DisplayName("Сравнить ожидаемый список Id с полученным после запроса")
    void compareExpectedListOfIdsAndActualTest() {
        List<Integer> expectedId = List.of(1, 2, 3, 4, 5, 6);
        Response response = given()
                .log().uri()
                .log().headers()
        .when()
                .get(UNKNOWN)
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();
        List<Integer> actualId = response.jsonPath().getList("data.id");
        assertEquals(expectedId, actualId);
    }
}
