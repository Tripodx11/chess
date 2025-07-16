package ServerTests;

import dataaccess.*;
import org.junit.jupiter.api.*;
import service.*;
import service.requests.RegisterRequest;
import service.results.RegisterAndLoginResult;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {

    private DataAccess dataAccess;
    private RegisterService service;

    @BeforeEach
    public void setup() {
        dataAccess = new SystemDataAccess(); // in-memory fake DB
        service = new RegisterService(dataAccess);
    }

    @Test
    public void testRegisterSuccess() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("user", "pass", "test email");

        RegisterAndLoginResult result = service.register(request);

        assertNotNull(result);
        assertEquals("user", result.getUsername());
        assertNotNull(result.getAuthToken());
        assertNull(result.getMessage());  // ✅ Should not be an error
    }

    @Test
    public void testRegisterDuplicateUsername() throws DataAccessException {
        RegisterRequest request = new RegisterRequest("user", "pass", "test email");
        RegisterAndLoginResult firstResult = service.register(request);
        assertNull(firstResult.getMessage());

        RegisterAndLoginResult duplicateResult = service.register(request);

        assertNull(duplicateResult.getUsername());
        assertNull(duplicateResult.getAuthToken());
        assertNotNull(duplicateResult.getMessage());
        assertTrue(duplicateResult.getMessage().contains("already"));
    }

    @Test
    public void testRegisterMissingField() throws DataAccessException {
        RegisterRequest badRequest = new RegisterRequest(null, "pass", "email@test.com");
        RegisterAndLoginResult result = service.register(badRequest);

        assertNotNull(result.getMessage());
        assertEquals("bad request", result.getMessage());
    }
}
