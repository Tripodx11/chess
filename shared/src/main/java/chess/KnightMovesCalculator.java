package chess;

import java.util.ArrayList;
import java.util.Collection;

import static chess.KingMovesCalculator.getChessMoves;

public class KnightMovesCalculator implements PieceMoveCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int[][] directions = {{1,-2}, {2,-1}, {2,1}, {1,2}, {-1,2}, {-2,1}, {-2,-1}, {-1,-2}};
        return getChessMoves(board, position, color, validMoves, directions);

    }
}
