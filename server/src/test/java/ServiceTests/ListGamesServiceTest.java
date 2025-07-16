package ServiceTests;

import dataaccess.DataAccess;
import dataaccess.SystemDataAccess;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ListGamesService;
import service.requests.LogoutAndListGamesRequest;
import service.results.ListGamesResult;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ListGamesServiceTest {

    private DataAccess dataAccess;
    private ListGamesService listGamesService;
    private String authToken = "token";

    @BeforeEach
    public void setup() {
        dataAccess = new SystemDataAccess();
        listGamesService = new ListGamesService(dataAccess);

        AuthData auth = new AuthData(authToken, "user");
        try {
            dataAccess.addAuth(auth);
        } catch (Exception e) {
            fail("Failed to set up auth token");
        }

        try {
            dataAccess.addGame(new GameData(1, null, null, "Test Game"));
        } catch (Exception e) {
            fail("Failed to setup game");
        }
    }

    @Test
    public void testListGamesSuccess() {
        LogoutAndListGamesRequest request = new LogoutAndListGamesRequest(authToken);
        ListGamesResult result = listGamesService.listGames(request);

        assertNull(result.getMessage());
        List<GameData> games = result.getGames();
        assertNotNull(games);
        assertEquals(1, games.size());
        assertEquals("Test Game", games.get(0).getGameName());
    }

    @Test
    public void testListGamesUnauthorized() {
        LogoutAndListGamesRequest request = new LogoutAndListGamesRequest("bad token");
        ListGamesResult result = listGamesService.listGames(request);

        assertEquals("unauthorized", result.getMessage());
        assertNull(result.getGames());
    }
}
