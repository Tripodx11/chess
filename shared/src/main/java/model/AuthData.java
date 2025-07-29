package model;

public class AuthData {

    public final String authToken;
    public final String username;

    public AuthData(String token, String un) {
        authToken = token;
        username = un;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }
}
