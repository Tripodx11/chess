package requests;

public class JoinGameRequest {

    private final String authToken;
    private final String playerColor;
    private final int gameID;

    public JoinGameRequest(String token, String color, int id) {
        authToken = token;
        playerColor = color;
        gameID = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }

}
