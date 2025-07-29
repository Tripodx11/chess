package results;

import model.GameData;

import java.util.List;

public class ListGamesResult {

    private final List<GameData> games;
    private final String message;

    public ListGamesResult (List<GameData> list) {
        games = list;
        message = null;
    }

    public ListGamesResult (String m) {
        games = null;
        message = m;
    }

    public List<GameData> getGames() {
        return games;
    }

    public String getMessage() {
        return message;
    }
}
