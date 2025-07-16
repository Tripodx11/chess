package chess;

import java.util.ArrayList;
import java.util.Collection;

public abstract class SlidingMoveCalculator implements PieceMoveCalculator{

    protected Collection<ChessMove> slidingMoveCalculator(ChessBoard board, ChessPosition position,
                                                          ChessGame.TeamColor color, int[][] directions) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        for (int[] direction : directions) {
            int newRow = position.getRow();
            int newCol = position.getColumn();
            while (true) {
                newRow = newRow + direction[0];
                newCol = newCol + direction[1];
                if (newRow > 8 || newRow < 1 || newCol > 8 || newCol < 1) {
                    break;
                }
                ChessPosition newMovePosition = new ChessPosition(newRow, newCol);
                ChessPiece newMovePiece = board.getPiece(newMovePosition);
                if (newMovePiece != null) {
                    if (newMovePiece.getTeamColor() == color) {
                        break;
                    }
                    else {
                        ChessMove move = new ChessMove(position, newMovePosition, null);
                        validMoves.add(move);
                        break;
                    }
                }
                ChessMove move = new ChessMove(position, newMovePosition, null);
                validMoves.add(move);
            }
        }
        return validMoves;
    }
}
