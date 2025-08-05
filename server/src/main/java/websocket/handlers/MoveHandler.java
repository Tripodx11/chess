package websocket.handlers;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.ConnectionManager;
import websocket.commands.MakeMoveCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.Collection;

public class MoveHandler {

    private final DataAccess dataAccess;
    private final ConnectionManager connections;

    public MoveHandler(DataAccess dataAccess, ConnectionManager connections) {
        this.dataAccess = dataAccess;
        this.connections = connections;
    }

    public void handle(MakeMoveCommand command, Session session) throws DataAccessException {
        try {
            String authToken = command.getAuthToken();
            int gameID = command.getGameID();

            // Validate auth token
            AuthData user = dataAccess.getAuthTokenUN(authToken);
            if (user == null) {
                connections.sendToSession(session, new ErrorMessage("Error: Invalid auth token."));
                return;
            }

            // Validate game
            GameData game = dataAccess.getGameData(gameID);
            if (game == null) {
                connections.sendToSession(session, new ErrorMessage("Error: Invalid game ID."));
                return;
            }

            ChessMove move = command.getMove();
            ChessGame gameLogic = game.getGame(); // The actual ChessGame instance

            if (game.getGame().isGameOver()) {
                connections.sendToSession(session, new ErrorMessage("Game is over. No more moves can be made."));
                return;
            }

            ChessGame.TeamColor playerColor = null;
            if (user.getUsername().equals(game.getWhiteUsername())) {
                playerColor = ChessGame.TeamColor.WHITE;
            } else if (user.getUsername().equals(game.getBlackUsername())) {
                playerColor = ChessGame.TeamColor.BLACK;
            } else {
                connections.sendToSession(session, new ErrorMessage("Error: You are not a player in this game."));
                return;
            }

            // Check if it's the user's turn
            if (gameLogic.getTeamTurn() != playerColor) {
                connections.sendToSession(session, new ErrorMessage("Error: It's not your turn."));
                return;
            }

            // Check if move is legal
            Collection<ChessMove> legalMoves = gameLogic.validMoves(move.getStartPosition());
            if (legalMoves.isEmpty()) {
                connections.sendToSession(session, new ErrorMessage("Error: Illegal move."));
                return;
            } else if (!legalMoves.contains(move)) {
                connections.sendToSession(session, new ErrorMessage("Error: Illegal move."));
                return;
            }

            // Apply the move
            gameLogic.makeMove(move);
            game.setGame(gameLogic); // Persist the updated state in the GameData object
            dataAccess.updateGameData(game);

            // Broadcast new game state to all clients
            LoadGameMessage updatedGameMessage = new LoadGameMessage(gameLogic, gameID);
            connections.broadcast(gameID, updatedGameMessage, null); // Send to all (no exclusions)

            // Notify clients
            String moveDescription = user.getUsername() + " moved from " +
                    formatPosition(move.getStartPosition()) + " to " + formatPosition(move.getEndPosition());

            connections.broadcast(gameID, new NotificationMessage(moveDescription), session);

            ChessGame.TeamColor nextTurn = gameLogic.getTeamTurn();

            if (gameLogic.isInCheckmate(nextTurn)) {
                String winner = nextTurn == ChessGame.TeamColor.WHITE ? "Black" : "White";
                game.getGame().setGameOver(true);
                dataAccess.updateGameData(game);
                connections.broadcast(gameID, new NotificationMessage("Checkmate! " + winner + " wins."), null);
                return;
            }

            if (gameLogic.isInStalemate(nextTurn)) {
                connections.broadcast(gameID, new NotificationMessage("Stalemate. The game is a draw."), null);
                game.getGame().setGameOver(true);
                dataAccess.updateGameData(game);
                return;
            }

            if (gameLogic.isInCheck(nextTurn)) {
                connections.broadcast(gameID, new NotificationMessage(nextTurn + " is in check!"), null);
            }


        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }

    private String formatPosition(ChessPosition pos) {
        char file = (char) ('a' + pos.getColumn() - 1); // column -> a-h
        int rank = pos.getRow(); // row -> 1-8
        return "" + file + rank;
    }

}