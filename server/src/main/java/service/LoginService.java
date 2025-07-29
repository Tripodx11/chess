package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySQLDataAccess;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import requests.LoginRequest;
import results.RegisterAndLoginResult;

public class LoginService {

    private final DataAccess dataAccess;

    public LoginService(DataAccess data) {
        dataAccess = data;
    }

    public RegisterAndLoginResult login(LoginRequest request) throws DataAccessException {

        //check for 400 error
        if (request.getUsername() == null || request.getUsername().isEmpty() || request.getPassword() == null || request.getPassword().isEmpty()) {
            return new RegisterAndLoginResult("bad request");
        }

        UserData existingUser = dataAccess.getUserData(request.getUsername());

        //check for 401 error with username not being registered
        if (existingUser == null) {
            return new RegisterAndLoginResult("unauthorized");
        }

        //check if password matches
        if (dataAccess instanceof MySQLDataAccess) {
            if (!BCrypt.checkpw(request.getPassword(), existingUser.getPassword())) {
                return new RegisterAndLoginResult("unauthorized");
            }
        } else {
            if (!request.getPassword().equals(existingUser.getPassword())) {
                return new RegisterAndLoginResult("unauthorized");
            }
        }

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
