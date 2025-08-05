package websocket.handlers;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.ConnectionManager;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class ConnectHandler {

    private final DataAccess dataAccess;
    private final ConnectionManager connections;

    public ConnectHandler(DataAccess dataAccess, ConnectionManager connections) {
        this.dataAccess = dataAccess;
        this.connections = connections;
    }

    public void handle(UserGameCommand command, Session session) throws DataAccessException {
        String authToken = command.getAuthToken();
        int gameID = command.getGameID();

        try {
            AuthData user = dataAccess.getAuthTokenUN(authToken);
            if (user == null) {
                sendError(session, "Error: Invalid auth token.");
                return;
            }

            GameData game = dataAccess.getGameData(gameID);
            if (game == null) {
                sendError(session, "Error: Invalid game ID.");
                return;
            }

            connections.add(gameID, session, user.getUsername());
            ChessGame currentGameState = game.getGame(); // Pull the full game object from GameData
            LoadGameMessage loadMessage = new LoadGameMessage(currentGameState, gameID);
            connections.sendToSession(session, loadMessage);

            // Determine role
            String role;
            if (user.getUsername().equals(game.getWhiteUsername())) {
                role = "White";
            } else if (user.getUsername().equals(game.getBlackUsername())) {
                role = "Black";
            } else {
                role = "Observer";
            }
            String message = user.getUsername() + " joined the game as " + role + ".";
            connections.broadcast(gameID, new NotificationMessage(message), session); // skip sending to self

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendError(Session session, String errorMsg) {
        connections.sendToSession(session, new ErrorMessage(errorMsg));
    }
}