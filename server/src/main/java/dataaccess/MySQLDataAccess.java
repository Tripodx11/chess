package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.Map;

public class MySQLDataAccess implements DataAccess{

    public MySQLDataAccess() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM auth_tokens");
            stmt.executeUpdate("DELETE FROM games");
            stmt.executeUpdate("DELETE FROM users");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to clear database", ex);
        }
    };

    //methods for adding data to models
    public void addUser (UserData data) throws DataAccessException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            String hashedPassword = BCrypt.hashpw(data.getPassword(), BCrypt.gensalt());

            stmt.setString(1, data.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, data.getEmail());

            stmt.executeUpdate();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                throw new DataAccessException("User already exists");
            }
            throw new DataAccessException("Unable to add user", ex);
        }
    };

    public void addAuth (AuthData data) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet");
    };

    public void addGame (GameData data) throws DataAccessException {
        throw new UnsupportedOperationException("Not implemented yet");
    };

    //methods for getting data from keys
    public UserData getUserData(String username) throws DataAccessException {

        String sql = "SELECT username, password, email FROM users WHERE username = ?";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String user = rs.getString("username");
                    String pass = rs.getString("password"); // we won't return this
                    String email = rs.getString("email");

                    return new UserData(user, null, email); // return null for password to avoid exposing it
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException("Unable to fetch user", ex);
        }
    };

    public AuthData getAuthTokenUN(String authToken) {
        return null;
    };

    public GameData getGameData(int gameID) {
        return null;
    };

    //unique game methods
    public Map<Integer, GameData> getAllGameData() {
        return null;
    };

    public int updateGameID() {
        return -2;
    };

    //remove methods
    public void removeAuthData (String authToken) {};
}
