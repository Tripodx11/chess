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
        dao.clear();
    }

    @Test
    public void addUser_positive() throws DataAccessException {
        UserData user = new UserData("user", "pass", "email");
        dao.addUser(user);

        UserData fetched = dao.getUserData("user");

        assertNotNull(fetched);
        assertEquals("user", fetched.getUsername());
        assertEquals("email", fetched.getEmail());
    }

    @Test
    public void addUser_duplicateUsername_negative() throws DataAccessException {
        UserData user1 = new UserData("user1", "pass1", "email1");
        UserData user2 = new UserData("user1", "pass2", "email2");

        dao.addUser(user1);

        DataAccessException thrown = assertThrows(DataAccessException.class, () -> {
            dao.addUser(user2);
        });

        assertTrue(thrown.getMessage().contains("already exists"));
    }
}