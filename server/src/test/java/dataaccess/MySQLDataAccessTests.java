package dataaccess;


import chess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLDataAccessTests {

    private DataAccess dao;

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
}