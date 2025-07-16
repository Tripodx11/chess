package chess;

import java.util.Collection;

public class RookMovesCalculator extends SlidingMoveCalculator implements PieceMoveCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        int[][] rookDirections = { {1,0}, {0,1}, {-1,0}, {0,-1} };
        return slidingMoveCalculator(board, position, color, rookDirections);
    }
}
