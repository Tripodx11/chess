package websocket.handlers;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.ConnectionManager;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

public class LeaveHandler {
    private final DataAccess dataAccess;
    private final ConnectionManager connections;

    public LeaveHandler(DataAccess dataAccess, ConnectionManager connections) {
        this.dataAccess = dataAccess;
        this.connections = connections;
    }

    public void handle(UserGameCommand command, Session session) {
        try {
            String authToken = command.getAuthToken();
            int gameID = command.getGameID();

            // Validate auth
            AuthData user = dataAccess.getAuthTokenUN(authToken);
            if (user == null) {
                sendError(session, "Error: Invalid auth token");
                return;
            }

            // Validate game
            GameData game = dataAccess.getGameData(gameID);
            if (game == null) {
                sendError(session, "Error: Invalid game ID");
                return;
            }

            // Remove session from connection manager
            connections.remove(gameID, user.getUsername());
            if (user.getUsername().equals(game.getWhiteUsername())) {
                game.setWhiteUsername(null);
            } else if (user.getUsername().equals(game.getBlackUsername())) {
                game.setBlackUsername(null);
            }

            dataAccess.updateGameData(game);


            // Notify others
            String msg = user.getUsername() + " has left the game.";
            connections.broadcast(gameID, new NotificationMessage(msg), session);

        } catch (DataAccessException e) {
            sendError(session, "Error: " + e.getMessage());
        }
    }

    private void sendError(Session session, String msg) {
        connections.sendToSession(session, new ErrorMessage(msg));
    }
}
