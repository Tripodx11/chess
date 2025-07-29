package requests;

public class CreateGameRequest {

    private final String authToken;
    private final String gameName;

    public CreateGameRequest(String token, String name) {
        authToken = token;
        gameName = name;
    }

    public CreateGameRequest(String name) {
        authToken = null;
        gameName = name;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getGameName() {
        return gameName;
    }

}
