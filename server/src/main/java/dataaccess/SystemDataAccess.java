package dataaccess;

import chess.ChessGame;
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
    private int gameID = 1;

    //clear method
    @Override
    public void clear(){
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
    public UserData getUserData(String username){
        return userData.get(username);
    }

    public AuthData getAuthTokenUN(String authToken){
        return authData.get(authToken);
    }

    public GameData getGameData(int gameID) {
        return gameData.get(gameID);
    }


    //unique methods for game logic
    public Map<Integer, GameData> getAllGameData() throws DataAccessException {
        return gameData;
    }

    public int updateGameID() {
        return gameID++;
    }

    public void updateGameUsername(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException {
        GameData game = gameData.get(gameID);

        if (game == null) {
            throw new DataAccessException("Game not found");
        }

        if (color == ChessGame.TeamColor.WHITE) {
            game.setWhiteUsername(username);
        } else if (color == ChessGame.TeamColor.BLACK) {
            game.setBlackUsername(username);
        } else {
            throw new DataAccessException("Invalid team color");
        }
    };

    @Override
    public void updateGameData(GameData game) throws DataAccessException {
        if (!gameData.containsKey(game.getGameID())) {
            throw new DataAccessException("Game not found");
        }
        gameData.put(game.getGameID(), game); // Replace the old game with the updated one
    }



    //remove methods
    public void removeAuthData (String authToken) throws DataAccessException {
        authData.remove(authToken);
    }

}
