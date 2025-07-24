package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import service.requests.LogoutAndListGamesRequest;
import service.results.ListGamesResult;

import java.util.ArrayList;
import java.util.List;

public class ListGamesService {

    private final DataAccess dataAccess;

    public ListGamesService(DataAccess data) {
        dataAccess = data;
    }

    public ListGamesResult listGames(LogoutAndListGamesRequest request) throws DataAccessException {

        //check for 400 error
        if (request.getAuthToken() == null || request.getAuthToken().isEmpty()) {
            return new ListGamesResult("bad request");
        }

        AuthData existingAuthData = dataAccess.getAuthTokenUN(request.getAuthToken());

        //401 error with wrong auth token
        if (existingAuthData == null) {
            return new ListGamesResult("unauthorized");
        }

        //return all games or that it didn't work
        try {
            List<GameData> games = new ArrayList<>(dataAccess.getAllGameData().values());
            return new ListGamesResult(games);
        } catch (Exception e) {
            return new ListGamesResult("internal issues: " + e.getMessage());
        }
    }
}
