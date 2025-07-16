package service;

import dataaccess.*;
import model.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {

    private DataAccess dataAccess;
    private ClearService service;

    @BeforeEach
    public void setup() {
        dataAccess = new SystemDataAccess(); // in-memory
        service = new ClearService(dataAccess);
    }

    @Test
    public void testClearSuccess() {
        try {
            // Add dummy data
            dataAccess.addUser(new UserData("user", "pass", "email"));
            dataAccess.addAuth(new AuthData("token", "user"));
            dataAccess.addGame(new GameData(1234, "white user", "black user", "testGame"));

            // Assert data was added
            assertNotNull(dataAccess.getUserData("user"));
            assertNotNull(dataAccess.getAuthTokenUN("token"));
            assertNotNull(dataAccess.getGameData(1234));

            // Clear it
            service.clear();

            // Assert data is cleared
            assertNull(dataAccess.getUserData("user"));
            assertNull(dataAccess.getAuthTokenUN("token"));
            assertNull(dataAccess.getGameData(1234));

        } catch (DataAccessException e) {
            fail("Clear threw an exception: " + e.getMessage());
        }
    }
}

