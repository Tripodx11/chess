package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import requests.CreateGameRequest;
import results.CreateGameResult;

public class CreateGameService {

    private final DataAccess dataAccess;

    public CreateGameService(DataAccess data) {
        dataAccess = data;
    }

    public CreateGameResult createGame(CreateGameRequest request) throws DataAccessException {

        //check for 400 error
        if (request.getAuthToken() == null || request.getAuthToken().isEmpty() || request.getGameName() == null || request.getGameName().isEmpty()) {
            return new CreateGameResult("bad request");
        }

        AuthData existingAuthData = dataAccess.getAuthTokenUN(request.getAuthToken());

        //401 error with wrong auth token
        if (existingAuthData == null) {
            return new CreateGameResult("unauthorized");
        }

        //add game and return that it worked or it didn't
        try {
            int gameID = dataAccess.updateGameID();
            dataAccess.addGame(new GameData(gameID, null, null, request.getGameName(), new ChessGame()));
            return new CreateGameResult(gameID);
        } catch (Exception e) {
            return new CreateGameResult("internal issues: " + e.getMessage());
        }
    }
}
