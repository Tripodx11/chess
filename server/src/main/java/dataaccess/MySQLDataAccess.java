package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MySQLDataAccess implements DataAccess{
    private int gameID = 1;

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
            throw new DataAccessException("Failed to clear database: ", ex);
        }
    }

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
    }

    public void addAuth (AuthData data) throws DataAccessException {
        String sql = "INSERT INTO auth_tokens (authToken, username) VALUES (?, ?)";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, data.getAuthToken());
            stmt.setString(2, data.getUsername());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                throw new DataAccessException("Auth Token already exists", ex);
            }
            throw new DataAccessException("Unable to authenticate user", ex);
        }
    }

    public void addGame (GameData data) throws DataAccessException {
        String sql = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            Gson gson = new Gson();
            String jsonGame = gson.toJson(data.getGame());

            stmt.setInt(1, data.getGameID());
            stmt.setString(2, data.getWhiteUsername());
            stmt.setString(3, data.getBlackUsername());
            stmt.setString(4, data.getGameName());
            stmt.setString(5, jsonGame);
            stmt.executeUpdate();

        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                throw new DataAccessException("Game already exists", ex);
            }
            throw new DataAccessException("Unable to authenticate user", ex);
        }
    }


    //methods for getting data from keys
    public UserData getUserData(String username) throws DataAccessException {

        String sql = "SELECT username, password, email FROM users WHERE username = ?";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    String user = resultSet.getString("username");
                    String pass = resultSet.getString("password");
                    String email = resultSet.getString("email");

                    return new UserData(user, pass, email);
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException("Unable to get user data: ", ex);
        }
    }

    public AuthData getAuthTokenUN(String authToken) throws DataAccessException {
        String sql = "SELECT authToken, username FROM auth_tokens WHERE authToken = ?";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, authToken);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    String user = resultSet.getString("username");
                    String auth = resultSet.getString("authToken");
                    return new AuthData(auth, user);
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException("Unable to get auth data: ", ex);
        }
    }

    public GameData getGameData(int gameID) throws DataAccessException {
        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameID);

            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    Gson gson = new Gson();
                    int id = resultSet.getInt("gameID");
                    String whiteUsername = resultSet.getString("whiteUsername");
                    String blackUsername = resultSet.getString("blackUsername");
                    String gameName = resultSet.getString("gameName");
                    ChessGame game = gson.fromJson(resultSet.getString("game"), ChessGame.class);
                    return new GameData(id, whiteUsername, blackUsername, gameName, game);
                } else {
                    return null;
                }
            }

        } catch (SQLException ex) {
            throw new DataAccessException("Unable to get game data: ", ex);
        } catch (Exception e) {
            throw new DataAccessException("exception: ", e);
        }
    }

    //unique game methods

    public Map<Integer, GameData> getAllGameData() throws DataAccessException {
        Map<Integer, GameData> games = new HashMap<>();

        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql);
             ResultSet resultSet = stmt.executeQuery();) {

            while (resultSet.next()) {
                Gson gson = new Gson();
                int id = resultSet.getInt("gameID");
                String name = resultSet.getString("gameName");
                String white = resultSet.getString("whiteUsername");
                String black = resultSet.getString("blackUsername");

                ChessGame game = gson.fromJson(resultSet.getString("game"), ChessGame.class);
                GameData gameData = new GameData(id, white, black, name, game);
                games.put(gameData.getGameID(), gameData);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to get all game data", e);
        }
        return games;
    }

    public void updateGameData(GameData gameData) throws DataAccessException {
        String sql = "UPDATE games SET game = ? WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            Gson gson = new Gson();
            String jsonGame = gson.toJson(gameData.getGame());

            stmt.setString(1, jsonGame);
            stmt.setInt(2, gameData.getGameID());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("No game with gameID: " + gameData.getGameID());
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update game data", e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int updateGameID() {
        return gameID++;
    }

    public void updateGameUsername(int gameID, ChessGame.TeamColor color, String username) throws DataAccessException {
        String column = (color == ChessGame.TeamColor.WHITE) ? "whiteUsername" : "blackUsername";
        String sql = "UPDATE games SET " + column + " = ? WHERE gameID = ?";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setInt(2, gameID);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("No game with gameID: " + gameID);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update game player", e);
        }
    }

    //remove methods
    public void removeAuthData(String authToken) throws DataAccessException {
        String sql = "DELETE FROM auth_tokens WHERE authToken = ?";

        try (var conn = DatabaseManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, authToken);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("No auth token found to delete");
            }

        } catch (SQLException ex) {
            throw new DataAccessException("Failed to remove auth token", ex);
        }
    }
}
