package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Map;

public class MySQLDataAccess implements DataAccess{

    public MySQLDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }

    public void clear() {};

    //methods for adding data to models
    public void addUser (UserData data) throws DataAccessException {};
    public void addAuth (AuthData data) throws DataAccessException {};
    public void addGame (GameData data) throws DataAccessException {};

    //methods for getting data from keys
    public UserData getUserData(String username) {
        return null;
    };

    public AuthData getAuthTokenUN(String authToken) {
        return null;
    };

    public GameData getGameData(int gameID) {
        return null;
    };

    //unique game methods
    public Map<Integer, GameData> getAllGameData() {
        return null;
    };

    public int updateGameID() {
        return -2;
    };

    //remove methods
    public void removeAuthData (String authToken) {};
}
