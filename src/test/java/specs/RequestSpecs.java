package specs;

import io.restassured.specification.RequestSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;

public class RequestSpecs {
    private static final String API_KEY = System.getenv("REQRES_API_KEY");
    public static RequestSpecification requestSpecification = with()
            .header("x-api-key", API_KEY)
            .filter(withCustomTemplates())
            .log().uri()
            .log().body()
            .log().headers()
            .contentType(JSON);
}

//RestAssured.requestSpecification = new RequestSpecBuilder()
//.addHeader("x-api-key", API_KEY).build();