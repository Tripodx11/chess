package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Map;

public interface DataAccess {

    public void clear() throws DataAccessException;

    //methods for adding data to models
    public void addUser (UserData data) throws DataAccessException;
    public void addAuth (AuthData data) throws DataAccessException;
    public void addGame (GameData data) throws DataAccessException;

    //methods for getting data from keys
    public UserData getUserData(String username) throws DataAccessException;
    public AuthData getAuthTokenUN(String authToken) throws DataAccessException;
    public GameData getGameData(int gameID) throws DataAccessException;

    //gets all game data
    public Map<Integer, GameData> getAllGameData();
}
