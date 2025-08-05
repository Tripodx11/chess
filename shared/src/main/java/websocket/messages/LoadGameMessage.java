package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    private final ChessGame game;
    private final int gameID;

    public LoadGameMessage(ChessGame game, int gameID) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.gameID = gameID;
    }

    public ChessGame getGame() { return game; }
    public int getGameID() { return gameID; }
}
