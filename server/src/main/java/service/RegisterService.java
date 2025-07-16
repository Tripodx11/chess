package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public class RegisterService {

    private final DataAccess dataAccess;

    public RegisterService(DataAccess data) {
        dataAccess = data;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        try {
            UserData existingUser = dataAccess.getUserData(request.getUsername());
            if (existingUser != null) {
                throw new DataAccessException("Username already taken");
            }
        } catch (DataAccessException e) {
            //this means the username was not found and we continue
        }

        UserData user = new UserData(request.getUsername(), request.getPassword(), request.getEmail());
        dataAccess.addUser(user);

        String authToken = AuthTokenGen.generateToken();
        AuthData authData = new AuthData(authToken, request.getUsername());
        dataAccess.addAuth(authData);

        return new RegisterResult(request.getUsername(), authToken);
    }
}
