package chess;

import java.util.Collection;

public class BishopMovesCalculator extends SlidingMoveCalculator implements PieceMoveCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        int [][] directions = {{1,-1}, {1,1}, {-1,1}, {-1,-1}};
        return slidingMoveCalculator(board, position, color, directions);
    }
}
