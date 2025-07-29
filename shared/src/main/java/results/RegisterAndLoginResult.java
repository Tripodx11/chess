package results;

public class RegisterAndLoginResult {

    private String username;
    private String authToken;
    private String message;

    public RegisterAndLoginResult(String un, String auth) {
        username = un;
        authToken = auth;
        message = null;
    }

    public RegisterAndLoginResult(String message) {
        this.message = message;
        username = null;
        authToken = null;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getMessage() {
        return message;
    }
}
