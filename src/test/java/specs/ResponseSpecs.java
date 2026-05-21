package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;

public class ResponseSpecs {
    public static ResponseSpecification responseSpec(int statusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .log(STATUS)
                .log(BODY)
                .build();
    }

    public static final ResponseSpecification
            status200 = responseSpec(200),
            status201 = responseSpec(201),
            status204 = responseSpec(204),
            status415 = responseSpec(415),
            status404 = responseSpec(404),
            status400 = responseSpec(400);

}
