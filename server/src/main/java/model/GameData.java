package model;

public class GameData {

    //create vars for data
    public final int gameID;
    public final String whiteUsername;
    public final String blackUsername;
    public final String gameName;

    //constructor
    public GameData(int ID, String whiteUN, String blackUN, String name) {
        gameID = ID;
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
}
