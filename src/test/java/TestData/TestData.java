package TestData;

import models.LoginRequest;
import models.UpdateDataRequest;

import static endpoints.Endpoints.USERS;
import static endpoints.Endpoints.USERS_PAGE;

public class TestData {
    public static final String
            UPDATE_USER_PATH = USERS + "2",
            USERS_PAGE_PATH = USERS_PAGE + "2",
            EMAIL = "michael.lawson@reqres.in",
            COLOUR_NAME = "cerulean",
            SUPPORT_URL = "https://benhowdle.im/first-cto-playbook?utm_source=reqres&utm_medium=json&utm_campaign=referral",
            NAME = "mike",
            JOB = "manager",
            MISSING_PASSWORD_MESSAGE = "Missing password",
            MISSING_EMAIL_MESSAGE = "Missing email or username",
            PASSWORD = "cityslicka",
            USER_NOT_FOUND = "user not found";

    public static UpdateDataRequest updateDataRequest() {
        UpdateDataRequest request = new UpdateDataRequest();
        request.setJob(JOB);
        request.setName(NAME);
        return request;
    }
    public static LoginRequest sendLoginWithoutPasswordRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail(EMAIL);
        return request;
    }

    public static LoginRequest sendPasswordWithoutLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setPassword(PASSWORD);
        return request;
    }
}
