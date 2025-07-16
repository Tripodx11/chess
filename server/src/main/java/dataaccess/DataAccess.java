package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Map;

public interface DataAccess {

    void clear();

    //methods for adding data to models
    void addUser (UserData data) throws DataAccessException;
    void addAuth (AuthData data) throws DataAccessException;
    void addGame (GameData data) throws DataAccessException;

    //methods for getting data from keys
    UserData getUserData(String username);
    AuthData getAuthTokenUN(String authToken);
    GameData getGameData(int gameID);

    //unique game methods
    Map<Integer, GameData> getAllGameData();
    int updateGameID();

    //remove methods
    void removeAuthData (String authToken);
}
