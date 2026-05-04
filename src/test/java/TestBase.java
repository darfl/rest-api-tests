import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .addHeader("x-api-key", "reqres_3b54ed9fda9845858b4d32e673518693").build();
    }
}
