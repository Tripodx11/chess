package service;

import dataaccess.DataAccess;
import model.AuthData;
import model.GameData;
import service.requests.CreateGameRequest;
import service.requests.JoinGameRequest;
import service.results.CreateGameResult;
import service.results.JoinGameResult;

public class JoinGameService {

    private final DataAccess dataAccess;

    public JoinGameService(DataAccess data) {
        dataAccess = data;
    }

    public JoinGameResult joinGame(JoinGameRequest request) {

        //check for 400 error for bad data inputted
        String color = request.getColor();
        color = color.trim();
        if (request.getAuthToken() == null || request.getAuthToken().isEmpty() || color == null || color.isEmpty()
                || !"white".equalsIgnoreCase(color) && !"black".equalsIgnoreCase(color) ||request.getGameID() < 0) {
            return new JoinGameResult("bad request");
        }

        GameData existingGameData = dataAccess.getGameData(request.getGameID());

        //400 error with wrong game id
        if (existingGameData == null) {
            return new JoinGameResult("bad request");
        }

        AuthData existingAuthData = dataAccess.getAuthTokenUN(request.getAuthToken());

        //401 error with wrong auth token
        if (existingAuthData == null) {
            return new JoinGameResult("unauthorized");
        }

        //403 error with color already being taken
        if ("white".equalsIgnoreCase(color) && existingGameData.getWhiteUsername() != null) {
            return new JoinGameResult("already taken");
        } else if ("black".equalsIgnoreCase(color) && existingGameData.getBlackUsername() != null) {
            return new JoinGameResult("already taken");
        }

        //join game and return that it worked or it didn't
        try {
            if ("white".equalsIgnoreCase(color)) {
                existingGameData.setWhiteUsername(existingAuthData.getUsername());
            } else if ("black".equalsIgnoreCase(color)) {
                existingGameData.setWhiteUsername(existingAuthData.getUsername());
            }
            return new JoinGameResult();
        } catch (Exception e) {
            return new JoinGameResult("internal issues: " + e.getMessage());
        }
    }
}
