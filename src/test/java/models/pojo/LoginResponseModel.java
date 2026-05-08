package models.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) //для игнора поля _meta в ответе json
public class LoginResponseModel {
    String token;

    public String getToken() {

        return token;
    }

    public void setToken(String token) {

        this.token = token;
    }

}
