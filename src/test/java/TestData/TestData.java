package TestData;

import models.LoginRequest;
import models.UpdateDataRequest;

import java.util.List;

import static endpoints.Endpoints.USERS;
import static endpoints.Endpoints.USERS_PAGE;

public class TestData {
    public static final List<String> EXPECTED_IDS = List.of("1", "2", "3", "4", "5", "6");

    public static final String
            UPDATE_USER_PATH = USERS + "2",
            USERS_PAGE_PATH = USERS_PAGE + "2",
            EMAIL = "michael.lawson@reqres.in",
            COLOUR_NAME = "cerulean",
            SUPPORT_URL = "https://benhowdle.im/first-cto-playbook?utm_source=reqres&utm_medium=json&utm_campaign=referral",
            NAME = "mike",
            JOB = "manager",
            LOGIN_EMAIL = "eve.holt@reqres.in",
            LOGIN_PASSWORD = "cityslicka",
            USER_NOT_FOUND = "user not found",
            MISSING_PASSWORD_MESSAGE = "Missing password",
            MISSING_EMAIL_MESSAGE = "Missing email or username",
            INVALID_EMAIL = "eve.ho@reqres.in",
            INVALID_PASSWORD = "city";

    public static UpdateDataRequest updateDataRequest() {
        UpdateDataRequest request = new UpdateDataRequest();
        request.setJob(JOB);
        request.setName(NAME);
        return request;
    }
    public static LoginRequest sendLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail(LOGIN_EMAIL);
        request.setPassword(LOGIN_PASSWORD);
        return request;
    }
    public static LoginRequest sendLoginWithInvalidEmailRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail(INVALID_EMAIL);
        request.setPassword(LOGIN_PASSWORD);
        return request;
    }

    public static LoginRequest sendLoginWithInvalidPasswordRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail(LOGIN_EMAIL);
        request.setPassword(INVALID_PASSWORD);
        return request;
    }

    public static LoginRequest sendLoginWithoutPasswordRequest() {
        LoginRequest request = new LoginRequest();
        request.setEmail(LOGIN_EMAIL);
        return request;
    }

    public static LoginRequest sendPasswordWithoutEmailRequest() {
        LoginRequest request = new LoginRequest();
        request.setPassword(LOGIN_PASSWORD);
        return request;
    }
}

