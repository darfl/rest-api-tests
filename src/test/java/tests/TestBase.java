package tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {

    //private static final String API_KEY = System.getenv("REQRES_API_KEY");

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        //RestAssured.requestSpecification = new RequestSpecBuilder()
                //.addHeader("x-api-key", API_KEY).build();
    }
}
