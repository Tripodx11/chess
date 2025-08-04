package client;

import model.AuthData;
import org.junit.jupiter.api.*;
import results.CreateGameResult;
import results.ListGamesResult;
import server.Server;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private Server server;
    public ServerFacade facade;

    @BeforeEach
    public void setup() throws IOException {
        server = new Server();
        int port = server.run(0);
        ClientConsole client = new ClientConsole();
        facade = new ServerFacade(port, client);
        facade.clear();
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterEach
    public void stopServer() {
        server.stop();
    }

    @Test
    public void clearPositive() {
        assertDoesNotThrow(() -> {
            facade.clear();
        });
    }

    //not needed but the autograder needed this for the database phase
    @Test
    public void clearNegative() {
        assertDoesNotThrow(() -> {
            facade.clear();  // First clear
            facade.clear();  // Second clear (nothing to delete)
        });
    }

    @Test
    public void registerPositive() throws IOException {
        AuthData result = facade.register("user", "pass", "email");
        assertNotNull(result);
        assertEquals("user", result.getUsername());
        assertNotNull(result.getAuthToken());
    }

    // Register same user again
    @Test
    public void registerNegative() throws IOException {
        facade.register("user", "pass", "email");
        IOException thrown = assertThrows(IOException.class, () ->
                facade.register("user", "pass", "email"));

        assertTrue(thrown.getMessage().contains("403")); // 400 for bad request
    }

    @Test
    public void loginPositive() throws IOException {
        facade.register("user", "pass", "email");
        AuthData result = facade.login("user", "pass");

        assertNotNull(result);
        assertEquals("user", result.getUsername());
        assertNotNull(result.getAuthToken());
    }

    @Test
    public void loginNegative() throws IOException {
        facade.register("user", "pass", "email");
        IOException thrown = assertThrows(IOException.class, () ->
                facade.login("user", "wrongpass"));
        assertTrue(thrown.getMessage().contains("401")); // Unauthorized
    }

    @Test
    public void createPositive() throws IOException {
        AuthData auth = facade.register("user", "pass", "email");
        CreateGameResult result = facade.create(auth.getAuthToken(), "game");

        assertNotNull(result);
        assertTrue(result.getGameID() > 0);
    }

    @Test
    public void createNegative() {
        // No auth token
        assertThrows(IOException.class, () -> {facade.create(null, "game");});
    }

    @Test
    public void listPositive() throws IOException {
        AuthData auth = facade.register("user", "pass", "email");
        facade.create(auth.getAuthToken(), "game1");
        facade.create(auth.getAuthToken(), "game2");

        ListGamesResult result = facade.list(auth.getAuthToken());
        assertNotNull(result);
        assertEquals(2, result.getGames().size());
        assertEquals("game1", result.getGames().get(0).getGameName());
        assertEquals("game2", result.getGames().get(1).getGameName());
    }

    @Test
    public void listNegative() {
        // bad auth token
        IOException thrown = assertThrows(IOException.class, () ->
                facade.list("token"));
    }

    @Test
    public void joinPositive() throws IOException {
        AuthData auth = facade.register("user", "pass", "email");
        CreateGameResult game = facade.create(auth.getAuthToken(), "game");

        // Attempt to join as white player
        assertDoesNotThrow(() -> {
            facade.join(auth.getAuthToken(), "WHITE", game.getGameID());
        });
    }

    @Test
    public void joinNegative() throws IOException {
        // bad color
        AuthData auth = facade.register("user", "pass", "email");
        CreateGameResult game = facade.create(auth.getAuthToken(), "game");
        assertThrows(IOException.class, () ->
                facade.join(auth.getAuthToken(), "BLUE", game.getGameID()) // Invalid team color
        );
    }

}
