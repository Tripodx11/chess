package chess;

import java.util.ArrayList;
import java.util.Collection;

public interface PieceMoveCalculator {

    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color);
}
