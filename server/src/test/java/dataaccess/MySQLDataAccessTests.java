package dataaccess;


import chess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLDataAccessTests {

    private DataAccess dao;

    @Test
    public void clearPositive() throws DataAccessException {
        UserData user = new UserData("user", "pass", "email");
        dao.addUser(user);
        dao.clear();
        assertNull(dao.getUserData("user"));
    }

    @Test
    public void clearNegative() throws DataAccessException {
        dao.clear();
        dao.clear();
        assertTrue(true);
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        dao = new MySQLDataAccess();
        dao.clear();
    }

    @Test
    public void addAndGetUserPositive() throws DataAccessException {
        UserData user = new UserData("user", "pass", "email");
        dao.addUser(user);

        UserData fetched = dao.getUserData("user");

        assertNotNull(fetched);
        assertEquals("user", fetched.getUsername());
        assertEquals("email", fetched.getEmail());
    }

    @Test
    public void addUserNegative() throws DataAccessException {
        //duplicate username
        UserData user1 = new UserData("user1", "pass1", "email1");
        UserData user2 = new UserData("user1", "pass2", "email2");

        dao.addUser(user1);

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> dao.addUser(user2));

        assertTrue(thrown.getMessage().contains("already exists"));
    }

    @Test
    public void getUserNegative() throws DataAccessException {
        //bad username
        UserData user1 = new UserData("user1", "pass1", "email1");
        dao.addUser(user1);
        UserData result = dao.getUserData("nonuser");
        assertNull(result);
    }

    @Test
    public void addAndGetAuthPositive() throws DataAccessException {
        UserData user = new UserData("user", "pass", "email");
        dao.addUser(user);
        AuthData authData = new AuthData("token", "user");
        dao.addAuth(authData);
        AuthData data = dao.getAuthTokenUN("token");

        assertNotNull(data);
        assertEquals("token", data.getAuthToken());
        assertEquals("user", data.getUsername());
    }

    @Test
    public void addAuthNegative() throws DataAccessException {
        //duplicate username
        UserData user = new UserData("user", "pass", "email");
        dao.addUser(user);
        AuthData authData = new AuthData("token", "user");
        AuthData authData2 = new AuthData("token", "user2");

        dao.addAuth(authData);

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> dao.addAuth(authData2));

        assertTrue(thrown.getMessage().contains("already exists"));
    }

    @Test
    public void getAuthNegative() throws DataAccessException {
        //bad token
        UserData user = new UserData("user", "pass", "email");
        dao.addUser(user);
        AuthData authData = new AuthData("token", "user");
        dao.addAuth(authData);
        AuthData result = dao.getAuthTokenUN("nontoken");
        assertNull(result);
    }

    @Test
    public void addAndGetGamePositive() throws DataAccessException {
        UserData user = new UserData("wuser", "pass", "email");
        dao.addUser(user);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(1, "wuser", "wuser", "game name", game);
        dao.addGame(gameData);
        GameData data = dao.getGameData(1);

        assertNotNull(data);
        assertEquals(1, data.getGameID());
        assertEquals("wuser", data.getWhiteUsername());
        assertEquals("wuser", data.getBlackUsername());
        assertInstanceOf(ChessGame.class, data.getGame());
    }

    @Test
    public void addGameNegative() throws DataAccessException {
        UserData user = new UserData("wuser", "pass", "email");
        dao.addUser(user);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(1, "wuser", "wuser", "game name", game);
        dao.addGame(gameData);
        GameData gameData2 = new GameData(1, "wuser", "wuser", "game name 2", game);

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> dao.addGame(gameData2));
        assert(thrown.getMessage().contains("already exists"));
    }

    @Test
    public void getGameNegative() throws DataAccessException {
        //bad token
        UserData user = new UserData("user", "pass", "email");
        dao.addUser(user);
        GameData gameData = new GameData(1, "user", "user", "game name", new ChessGame());
        dao.addGame(gameData);
        GameData result = dao.getGameData(10);
        assertNull(result);
    }

    @Test
    public void getAllGameDataPositive() throws DataAccessException {
        UserData user = new UserData("user", "pass", "email");
        dao.addUser(user);
        GameData gameData = new GameData(1, "user", "user", "game name 1", new ChessGame());
        dao.addGame(gameData);
        GameData gameData2 = new GameData(2, "user", "user", "game name 2", new ChessGame());
        dao.addGame(gameData2);

        Map<Integer, GameData> allGameData = dao.getAllGameData();

        assertEquals(2, allGameData.size());
        assertTrue(allGameData.containsKey(1));
        assertTrue(allGameData.containsKey(2));
        assertEquals("user", allGameData.get(1).getWhiteUsername());
        assertEquals("user", allGameData.get(2).getWhiteUsername());
        assertEquals("user", allGameData.get(1).getBlackUsername());
        assertEquals("user", allGameData.get(2).getBlackUsername());
        assertEquals("game name 1", allGameData.get(1).getGameName());
        assertEquals("game name 2", allGameData.get(2).getGameName());
    }

    @Test
    public void getAllGameDataNegative() throws DataAccessException {
        Map<Integer, GameData> allGameData = dao.getAllGameData();
        assertNotNull(allGameData, "Expected an empty map, but got null");
        assertTrue(allGameData.isEmpty(), "Expected no games, but found some");
    }

    @Test
    public void updateGameIDTest() {
        int id = dao.updateGameID();
        assertEquals(1, id);
    }

    @Test
    public void removeAuthDataPositive() throws DataAccessException {
        UserData user = new UserData("user", "pass", "email");
        dao.addUser(user);
        AuthData auth = new AuthData("token", "user");
        dao.addAuth(auth);

        AuthData data = dao.getAuthTokenUN("token");
        assertNotNull(data);
        assertEquals("user", data.getUsername());

        dao.removeAuthData("token");
        AuthData deleted = dao.getAuthTokenUN("token");
        assertNull(deleted);
    }
    @Test
    public void removeAuthDataNegative() throws DataAccessException {
        UserData user = new UserData("user", "pass", "email");
        dao.addUser(user);
        DataAccessException thrown = assertThrows(DataAccessException.class, () -> dao.removeAuthData("bad token"));
        assert(thrown.getMessage().contains("No auth token found to delete"));
    }

    @Test
    public void testUpdateGameUsernamePositive() throws DataAccessException {
        UserData user = new UserData("user", "pass", "email");
        dao.addUser(user);
        AuthData auth = new AuthData("token", "user");
        dao.addAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(1, null, null, "Test Game", game);
        dao.addGame(gameData);
        dao.updateGameUsername(1, ChessGame.TeamColor.WHITE, user.getUsername());
        GameData updated = dao.getGameData(1);
        assertEquals("user", updated.getWhiteUsername());
    }

    @Test
    public void testUpdateGameUsernameNegative() throws DataAccessException {
        UserData user = new UserData("user", "pass", "email");
        dao.addUser(user);

        DataAccessException thrown = assertThrows(DataAccessException.class,
                () -> dao.updateGameUsername(10, ChessGame.TeamColor.BLACK, user.getUsername()));
        assert(thrown.getMessage().contains("No game with gameID"));
    }

}