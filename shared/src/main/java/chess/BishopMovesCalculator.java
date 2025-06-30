package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMoveCalculator{

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {
        Collection<ChessMove> validMoves = new ArrayList<>();

        //Up and left diagonal
        //ChessPosition startPosition = new ChessPosition(position.getRow(), position.getColumn());
        int startRow = position.getRow();
        int startCol = position.getColumn();
        while (startRow < 8 && startCol > 1 ) {
            int newRow = startRow + 1;
            int newCol = startCol - 1;
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
            startRow ++;
            startCol --;
        }

        //Up and right diagonal
        startRow = position.getRow();
        startCol = position.getColumn();
        while (startRow < 8 && startCol < 8 ) {
            int newRow = startRow + 1;
            int newCol = startCol + 1;
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
            startRow ++;
            startCol ++;
        }

        //Down and right diagonal
        startRow = position.getRow();
        startCol = position.getColumn();
        while (startRow > 1 && startCol < 8 ) {
            int newRow = startRow - 1;
            int newCol = startCol + 1;
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
            startRow --;
            startCol ++;
        }

        //Down and left diagonal
        startRow = position.getRow();
        startCol = position.getColumn();
        while (startRow > 1 && startCol > 1 ) {
            int newRow = startRow - 1;
            int newCol = startCol - 1;
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
            startRow --;
            startCol --;
        }
        System.out.println(validMoves);

        return validMoves;
    }
}
