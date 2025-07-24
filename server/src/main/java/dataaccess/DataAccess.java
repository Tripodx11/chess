package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Map;

public interface DataAccess {

    void clear() throws DataAccessException;

    //methods for adding data to models
    void addUser (UserData data) throws DataAccessException;
    void addAuth (AuthData data) throws DataAccessException;
    void addGame (GameData data) throws DataAccessException;

    //methods for getting data from keys
    UserData getUserData(String username) throws DataAccessException;
    AuthData getAuthTokenUN(String authToken) throws DataAccessException;
    GameData getGameData(int gameID) throws DataAccessException;

    //unique game methods
    Map<Integer, GameData> getAllGameData() throws DataAccessException;
    int updateGameID();
    void updateGameUsername(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException;


    //remove methods
    void removeAuthData (String authToken) throws DataAccessException ;
}
