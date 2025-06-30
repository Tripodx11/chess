package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMoveCalculator{

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        Collection<ChessMove> validMoves = new ArrayList<>();
        int [][] directions = {{1,0}, {0,1}, {-1,0}, {0,-1}, {1,-1}, {1,1}, {-1,1}, {-1,-1}};

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
