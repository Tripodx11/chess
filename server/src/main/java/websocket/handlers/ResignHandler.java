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

public class ResignHandler {
    private final DataAccess dataAccess;
    private final ConnectionManager connections;

    public ResignHandler(DataAccess dataAccess, ConnectionManager connections) {
        this.dataAccess = dataAccess;
        this.connections = connections;
    }

    public void handle(UserGameCommand command, Session session) {
        try {
            String authToken = command.getAuthToken();
            int gameID = command.getGameID();

            // Validate auth token
            AuthData user = dataAccess.getAuthTokenUN(authToken);
            if (user == null) {
                sendError(session, "Error: Invalid auth token.");
                return;
            }

            // Validate game
            GameData game = dataAccess.getGameData(gameID);
            if (game == null) {
                sendError(session, "Error: Invalid game ID.");
                return;
            }

            if (game.getGame().isGameOver()) {
                sendError(session, "Error: Game is already over.");
                return;
            }

            // Check if user is a player (white or black)
            String resigningUser = user.getUsername();
            String white = game.getWhiteUsername();
            String black = game.getBlackUsername();

            if (!resigningUser.equals(white) && !resigningUser.equals(black)) {
                sendError(session, "Only players may resign from a game");
                return;
            }


            // Mark game as over
            game.getGame().setGameOver(true);
            dataAccess.updateGameData(game);

            String resignMessage = resigningUser + " has resigned. Game over.";

            // Broadcast resignation to all *except* the resigning player
            connections.broadcast(gameID, new NotificationMessage(resignMessage), null);

        } catch (DataAccessException e) {
            sendError(session, "Error: " + e.getMessage());
        }
    }

    private void sendError(Session session, String message) {
        connections.sendToSession(session, new ErrorMessage(message));
    }
}
