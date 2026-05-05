import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class DeleteTests extends TestBase{

    @Test
    @DisplayName("Удалить ресурс" )
    void deleteResourcesTest() {
        given()
                .log().all()
        .when()
                .delete("/users/2")

        .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}
