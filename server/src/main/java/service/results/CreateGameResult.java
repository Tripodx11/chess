package service.results;

public class CreateGameResult {

    private final int gameID;
    private final String message;

    public CreateGameResult (int id) {
        gameID = id;
        message = null;
    }

    public CreateGameResult (String m) {
        gameID = -1;
        message = m;
    }

    public int getGameID() {
        return gameID;
    }

    public String getMessage() {
        return message;
    }
}
