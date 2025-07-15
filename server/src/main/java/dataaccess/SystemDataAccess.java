package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class SystemDataAccess implements DataAccessInterface {

    private final Map<String, UserData> userData = new HashMap<>();
    private final Map<String, AuthData> authTokens = new HashMap<>();
    private final Map<Integer, GameData> gameData = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        userData.clear();
        authTokens.clear();
        gameData.clear();
    }
}
