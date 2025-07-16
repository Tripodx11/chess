package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.requests.RegisterRequest;
import service.results.RegisterAndLoginResult;

public class RegisterService {

    private final DataAccess dataAccess;

    public RegisterService(DataAccess data) {
        dataAccess = data;
    }

    public RegisterAndLoginResult register(RegisterRequest request) throws DataAccessException {

        //check for 400 error
        if (request.getUsername() == null || request.getUsername().isEmpty() || request.getPassword() == null || request.getPassword().isEmpty() || request.getEmail() == null || request.getEmail().isEmpty()) {
            return new RegisterAndLoginResult("bad request");
        }

        UserData existingUser = dataAccess.getUserData(request.getUsername());
        if (existingUser != null) {
            return new RegisterAndLoginResult("already taken");
        }

        try {
            UserData user = new UserData(request.getUsername(), request.getPassword(), request.getEmail());
            dataAccess.addUser(user);

            String authToken = AuthTokenGen.generateToken();
            AuthData authData = new AuthData(authToken, request.getUsername());
            dataAccess.addAuth(authData);

            return new RegisterAndLoginResult(request.getUsername(), authToken);
        } catch (DataAccessException e) {
            return new RegisterAndLoginResult("internal issues: " + e.getMessage());
        }
    }
}
