package ServiceTests;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.SystemDataAccess;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LogoutService;
import service.requests.LogoutRequest;
import service.results.LogoutResult;

import static org.junit.jupiter.api.Assertions.*;

public class LogoutServiceTest {

    private DataAccess dataAccess;
    private LogoutService logoutService;
    private String token;

    @BeforeEach
    public void setup() throws DataAccessException {
        dataAccess = new SystemDataAccess();
        logoutService = new LogoutService(dataAccess);

        token = "test token";
        AuthData authData = new AuthData(token, "user1");
        try {
            dataAccess.addAuth(authData);
        } catch (Exception e) {
            fail("Failed to set up test auth data");
        }
    }

    @Test
    public void testLogoutSuccess() {
        LogoutRequest request = new LogoutRequest(token);
        LogoutResult result = logoutService.logout(request);

        assertNull(result.getMessage());
        assertNull(dataAccess.getAuthTokenUN(token));
    }

    @Test
    public void testLogoutInvalidToken() {
        LogoutRequest request = new LogoutRequest("bad token test");
        LogoutResult result = logoutService.logout(request);

        assertEquals("unauthorized", result.getMessage());
    }
}
