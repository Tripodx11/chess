package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.requests.RegisterRequest;
import service.results.RegisterAndLoginResult;

public class LoginService {

    private final DataAccess dataAccess;

    public LoginService(DataAccess data) {
        dataAccess = data;
    }

    public RegisterAndLoginResult login(RegisterRequest request) throws DataAccessException {

        //check for 400 error
        if (request.getUsername() == null || request.getUsername().isEmpty() || request.getPassword() == null || request.getPassword().isEmpty() {
            return new RegisterAndLoginResult("bad request");
        }

        //check for 401 error with username not being registered
        try {
            UserData existingUser = dataAccess.getUserData(request.getUsername());
        } catch (DataAccessException e) {
            //this means the username was not found
            return new RegisterAndLoginResult("unauthorized");
        }

        UserData existingUser = dataAccess.getUserData(request.getUsername());

        //check if password matches
        if (!request.getPassword().equals(existingUser.getPassword())) {
            return new RegisterAndLoginResult("unauthorized");
        } else {
            try {
                String authToken = AuthTokenGen.generateToken();
                AuthData authData = new AuthData(authToken, request.getUsername());
                dataAccess.addAuth(authData);
                return new RegisterAndLoginResult(request.getUsername(), authToken);
            } catch (DataAccessException e) {
                return new RegisterAndLoginResult("internal issues: " + e.getMessage());
            }

        }
    }
}
