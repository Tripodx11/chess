package chess;

import java.util.Collection;

public class QueenMovesCalculator extends SlidingMoveCalculator implements PieceMoveCalculator{

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        int [][] directions = {{1,0}, {0,1}, {-1,0}, {0,-1}, {1,-1}, {1,1}, {-1,1}, {-1,-1}};
        return slidingMoveCalculator(board, position, color, directions);
    }
}
