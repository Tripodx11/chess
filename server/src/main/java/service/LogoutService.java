package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import service.requests.LogoutAndListGamesRequest;
import service.results.LogoutResult;

public class LogoutService {

    private final DataAccess dataAccess;

    public LogoutService(DataAccess data) {
        dataAccess = data;
    }

    public LogoutResult logout(LogoutAndListGamesRequest request) throws DataAccessException {

        //check for 400 error
        if (request.getAuthToken() == null || request.getAuthToken().isEmpty()) {
            return new LogoutResult("bad request");
        }

        AuthData existingAuthData = dataAccess.getAuthTokenUN(request.getAuthToken());

        //401 error with wrong auth token
        if (existingAuthData == null) {
            return new LogoutResult("unauthorized");
        }

        //remove and return that it worked or it didn't
        try {
            dataAccess.removeAuthData(request.getAuthToken());
            return new LogoutResult();
        } catch (Exception e) {
            return new LogoutResult("internal issues: " + e.getMessage());
        }
    }
}
