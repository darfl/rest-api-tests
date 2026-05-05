import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetResourcesTests extends TestBase {

    @Test
    @DisplayName("Получить размер всего списка ресурсов" )
    void getNumOfResourcesTest() {
        int totalResources = 12;
        given()
                .log().all()
        .when()
                .get("/unknown")

        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(totalResources));
    }

    @Test
    @DisplayName("Получить размер списка ресурсов на странице")
    void getNumOfResourcesOnPageTest() {
        int numberOfResourcesOnPage= 6;
        given()
                .log().uri()
        .when()
                .get("unknown")
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
        .when()
                .get("unknown")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().response();
        List<Integer> actualId = response.jsonPath().getList("data.id");
        assertEquals(expectedId, actualId);
    }
}
