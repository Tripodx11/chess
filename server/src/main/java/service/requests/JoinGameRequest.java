package service.requests;

import service.results.JoinGameResult;

import java.util.Objects;

public class JoinGameRequest {

    private final String authToken;
    private final String color;
    private final int gameID;

    public JoinGameRequest(String token, String color, int id) {
        authToken = token;
        this.color = color;
        gameID = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getColor() {
        return color;
    }

    public int getGameID() {
        return gameID;
    }

}
