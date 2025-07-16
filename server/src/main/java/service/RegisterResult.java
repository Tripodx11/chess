package service;

public class RegisterResult {

    private String username;
    private String authToken;

    public RegisterResult (String un, String auth) {
        username = un;
        authToken = auth;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
