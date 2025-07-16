package model;

public class GameData {

    //create vars for data
    public final int gameID;
    public String whiteUsername;
    public String blackUsername;
    public final String gameName;

    //constructor
    public GameData(int id, String whiteUN, String blackUN, String name) {
        gameID = id;
        whiteUsername = whiteUN;
        blackUsername = blackUN;
        gameName = name;
    }

    public int getGameID() {
        return gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public void setWhiteUsername(String username) {
        whiteUsername = username;
    }

    public void setBlackUsername(String username) {
        blackUsername = username;
    }

}
