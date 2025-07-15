package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class SystemDataAccess implements DataAccess {

    //var set up
    private final Map<String, UserData> userData = new HashMap<>();
    private final Map<String, AuthData> authData = new HashMap<>();
    private final Map<Integer, GameData> gameData = new HashMap<>();

    //clear method
    @Override
    public void clear() throws DataAccessException {
        userData.clear();
        authData.clear();
        gameData.clear();
    }



    //methods for adding data to models
    public void addUser (UserData data) throws DataAccessException{
        if (userData.containsKey(data.getUsername())) {
            throw new DataAccessException("User already exists");
        }
        userData.put(data.getUsername(), data);
        System.out.println(userData);
    }

    public void addAuth (AuthData data) throws DataAccessException{
        if (authData.containsKey(data.getUsername())) {
            throw new DataAccessException("Auth token already exists");
        }
        authData.put(data.getAuthToken(), data);
    }

    public void addGame (GameData data) throws DataAccessException{
        if (gameData.containsKey(data.getGameID())) {
            throw new DataAccessException("Game already exists");
        }
        gameData.put(data.getGameID(), data);
    }



    //getting data methods
    public UserData getUserData(String username) throws DataAccessException {
        if (!userData.containsKey(username)) {
            throw new DataAccessException("User not found");
        }
        return userData.get(username);
    }

    public AuthData getAuthTokenUN(String authToken) throws DataAccessException {
        if (!authData.containsKey(authToken)) {
            throw new DataAccessException("Invalid auth token");
        }
        return authData.get(authToken);
    }

    public GameData getGameData(int gameID) throws DataAccessException {
        if (!gameData.containsKey(gameID)) {
            throw new DataAccessException("Invalid Game ID");
        }
        return gameData.get(gameID);
    }

    public Map<Integer, GameData> getAllGameData() {
        return gameData;
    }
}
