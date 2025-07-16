package ServiceTests;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.SystemDataAccess;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoginService;
import service.requests.LoginRequest;
import service.results.RegisterAndLoginResult;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    private DataAccess dataAccess;
    private LoginService loginService;

    @BeforeEach
    public void setup() {
        dataAccess = new SystemDataAccess();
        loginService = new LoginService(dataAccess);

        // Create a user directly in the fake DB
        UserData user = new UserData("user1", "pass1", "user1@example.com");
        try {
            dataAccess.addUser(user);
        } catch (Exception e) {
            fail("Failed to set up test user");
        }
    }

    @Test
    public void testLoginSuccess() throws DataAccessException {
        LoginRequest request = new LoginRequest("user", "pass");
        RegisterAndLoginResult result = loginService.login(request);

        assertNull(result.getMessage()); // No error
        assertEquals("user", result.getUsername());
        assertNotNull(result.getAuthToken());
    }

    @Test
    public void testLoginInvalidPassword() throws DataAccessException {
        LoginRequest request = new LoginRequest("user", "pass1");
        RegisterAndLoginResult result = loginService.login(request);

        assertNull(result.getUsername());
        assertNull(result.getAuthToken());
        assertEquals("unauthorized", result.getMessage());
    }
}