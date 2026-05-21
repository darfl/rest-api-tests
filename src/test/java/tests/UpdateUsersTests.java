package tests;

import models.UpdateDataResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static TestData.TestData.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static specs.RequestSpecs.requestSpecification;
import static specs.ResponseSpecs.responseSpec;

public class UpdateUsersTests extends TestBase {
    @Test
    @DisplayName("Проверка обновления имени и должности пользователя")
    void updateNameAndJobTest() {
        UpdateDataResponse response =
        step("Отправка запроса на обновление данных пользователя", () ->
            given(requestSpecification)
                .body(updateDataRequest())
            .when()
                .put(UPDATE_USER_PATH)
            .then()
                .spec(responseSpec(200))
                .extract().as(UpdateDataResponse.class));
        step("Проверка имени", () ->
                assertThat(response.getName(), is(NAME)));
        step("Проверка должности", () ->
                assertThat(response.getJob(), is(JOB)));
    }
}
