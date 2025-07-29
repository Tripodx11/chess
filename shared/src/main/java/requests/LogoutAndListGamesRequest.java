package requests;

public class LogoutAndListGamesRequest {

    private final String authToken;

    public LogoutAndListGamesRequest(String token) {
        authToken = token;
    }

    public String getAuthToken() {
        return authToken;
    }
}
