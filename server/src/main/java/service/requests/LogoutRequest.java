package service.requests;

public class LogoutRequest {

    private final String authToken;

    public LogoutRequest (String token) {
        authToken = token;
    }

    public String getAuthToken() {
        return authToken;
    }
}
