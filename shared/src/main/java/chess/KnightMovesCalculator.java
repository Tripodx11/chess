package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMoveCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int[][] directions = {{1,-2}, {2,-1}, {2,1}, {1,2}, {-1,2}, {-2,1}, {-2,-1}, {-1,-2}};
        int startRow = position.getRow();
        int startCol = position.getColumn();

        for (int[] direction : directions) {
            int newRow = startRow + direction[0];
            int newCol = startCol + direction[1];
            if (newRow > 0 && newRow < 9 && newCol > 0 && newCol < 9) {
                ChessPosition newMovePosition = new ChessPosition(newRow, newCol);
                ChessPiece newMovePiece = board.getPiece(newMovePosition);

                if (newMovePiece != null && newMovePiece.getTeamColor() != color) {
                    ChessMove move = new ChessMove(position, newMovePosition, null);
                    validMoves.add(move);
                } else if (newMovePiece == null) {
                    //code that runs if the position being looked at is empty on the board
                    ChessMove move = new ChessMove(position, newMovePosition, null);
                    validMoves.add(move);
                }
            }
        }

        return validMoves;

    }
}
