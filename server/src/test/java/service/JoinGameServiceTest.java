package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.SystemDataAccess;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.JoinGameRequest;
import results.JoinGameResult;

import static org.junit.jupiter.api.Assertions.*;

public class JoinGameServiceTest {

    private DataAccess dataAccess;
    private JoinGameService joinGameService;
    private int gameID;
    private String authToken;
    private final String username = "user";

    @BeforeEach
    public void setup() throws DataAccessException {
        dataAccess = new SystemDataAccess();
        joinGameService = new JoinGameService(dataAccess);
        authToken = "token";
        AuthData authData = new AuthData(authToken, username);

        try {
            dataAccess.addAuth(authData);
        } catch (Exception e) {
            fail("Failed to set up auth token");
        }

        gameID = 1;
        GameData gameData = new GameData(gameID, null, null, "test game", new ChessGame());
        try {
            dataAccess.addGame(gameData);
        } catch (Exception e) {
            fail("Failed to setup game");
        }
    }

    @Test
    public void testJoinGameSuccess() throws DataAccessException {
        JoinGameRequest request = new JoinGameRequest(authToken, "white", gameID);
        JoinGameResult result = joinGameService.joinGame(request);

        assertNull(result.getMessage());
        GameData joined = dataAccess.getGameData(gameID);
        assertEquals(username, joined.getWhiteUsername());
    }

    @Test
    public void testJoinGameBadColor() throws DataAccessException {
        JoinGameRequest request = new JoinGameRequest(authToken, "bad color", gameID);
        JoinGameResult result = joinGameService.joinGame(request);

        assertEquals("bad request", result.getMessage());
    }
}
