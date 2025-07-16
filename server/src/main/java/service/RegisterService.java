package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.requests.RegisterRequest;
import service.results.RegisterResult;

public class RegisterService {

    private final DataAccess dataAccess;

    public RegisterService(DataAccess data) {
        dataAccess = data;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException {

        //check for 400 error
        if (request.getUsername() == null || request.getUsername().isEmpty() || request.getPassword() == null || request.getPassword().isEmpty() || request.getEmail() == null || request.getEmail().isEmpty()) {
            return new RegisterResult("bad request");
        }

        try {
            UserData existingUser = dataAccess.getUserData(request.getUsername());
            if (existingUser != null) {
                return new RegisterResult("already taken");
            }
        } catch (DataAccessException e) {
            //this means the username was not found and we continue
        }

        try {
            UserData user = new UserData(request.getUsername(), request.getPassword(), request.getEmail());
            dataAccess.addUser(user);

            String authToken = AuthTokenGen.generateToken();
            AuthData authData = new AuthData(authToken, request.getUsername());
            dataAccess.addAuth(authData);

            return new RegisterResult(request.getUsername(), authToken);
        } catch (DataAccessException e) {
            return new RegisterResult("internal issues: " + e.getMessage());
        }
    }
}
