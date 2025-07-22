package passoff.server;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MySQLDataAccess;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLDataAccessTests {

    private DataAccess dao;

    @BeforeEach
    public void setup() throws DataAccessException {
        dao = new MySQLDataAccess();
        dao.clear(); // Clean DB before each test
    }

    @Test
    public void addUser_positive() throws DataAccessException {
        UserData user = new UserData("testuser", "password123", "test@example.com");
        dao.addUser(user);

        UserData fetched = dao.getUserData("testuser");

        assertNotNull(fetched);
        assertEquals("testuser", fetched.getUsername());
        assertEquals("test@example.com", fetched.getEmail());
        // Do NOT assert password equality since it's hashed
    }

    @Test
    public void addUser_duplicateUsername_negative() throws DataAccessException {
        UserData user1 = new UserData("dupeuser", "pass1", "dupe1@example.com");
        UserData user2 = new UserData("dupeuser", "pass2", "dupe2@example.com");

        dao.addUser(user1);

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> {
            dao.addUser(user2);
        });

        assertTrue(thrown.getMessage().contains("already exists"));
    }
}