package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.SystemDataAccess;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.requests.CreateGameRequest;
import service.results.CreateGameResult;

import static org.junit.jupiter.api.Assertions.*;

public class CreateGameServiceTest {

    private DataAccess dataAccess;
    private CreateGameService createGameService;
    private String token;

    @BeforeEach
    public void setup() throws DataAccessException {
        dataAccess = new SystemDataAccess();
        createGameService = new CreateGameService(dataAccess);

        token = "test token";
        AuthData authData = new AuthData(token, "user");
        try {
            dataAccess.addAuth(authData);
        } catch (Exception e) {
            fail("Failed to set up auth token");
        }
    }

    @Test
    public void testCreateGameSuccess() throws DataAccessException {
        CreateGameRequest request = new CreateGameRequest(token, "My Game");
        CreateGameResult result = createGameService.createGame(request);

        assertNull(result.getMessage());
        assertTrue(result.getGameID() > 0);

        GameData gameData = dataAccess.getGameData(result.getGameID());
        assertEquals("My Game", gameData.getGameName());
    }

    @Test
    public void testCreateGameInvalidToken() throws DataAccessException {
        CreateGameRequest request = new CreateGameRequest("bad test token", "My Game");
        CreateGameResult result = createGameService.createGame(request);

        assertEquals("unauthorized", result.getMessage());
        assertEquals(-1, result.getGameID());
    }
}
