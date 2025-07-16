package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements  PieceMoveCalculator{

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {

        Collection<ChessMove> validMoves = new ArrayList<>();
        int startRow = position.getRow();
        int startCol = position.getColumn();
        int newRow = startRow +1;

        if (color == ChessGame.TeamColor.WHITE) {
            if (startRow < 8) {
                ChessPosition newMovePosition = new ChessPosition(newRow, startCol);
                ChessPiece newMovePiece = board.getPiece(newMovePosition);
                handleForwardMoves(validMoves, board, position, startRow + 1, startRow, +1, 8, 2);
                diagonalMoves(validMoves, board, position, startRow + 1, startCol - 1, color, 8);
                diagonalMoves(validMoves, board, position, startRow + 1, startCol + 1, color, 8);
            }
        }

        if (color == ChessGame.TeamColor.BLACK) {
            newRow = startRow - 1;

            if (startRow > 1) {
                ChessPosition newMovePosition = new ChessPosition(newRow, startCol);
                ChessPiece newMovePiece = board.getPiece(newMovePosition);
                handleForwardMoves(validMoves, board, position, startRow - 1, startRow, -1, 1, 7);
                diagonalMoves(validMoves, board, position, startRow - 1, startCol - 1, color, 1);
                diagonalMoves(validMoves, board, position, startRow - 1, startCol + 1, color, 1);

            }
        }
        return validMoves;
    }

    private void addPromotionMoves(Collection<ChessMove> moves, ChessPosition start, ChessPosition end) {
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
    }

    private void handleForwardMoves(Collection<ChessMove> moves, ChessBoard board,
                                    ChessPosition start, int newRow, int startRow, int direction,
                                    int promotionRow, int doubleMoveRow) {

        int col = start.getColumn();
        ChessPosition oneForward = new ChessPosition(newRow, col);
        ChessPiece pieceAtNew = board.getPiece(oneForward);

        if (pieceAtNew == null) {
            if (newRow == promotionRow) {
                addPromotionMoves(moves, start, oneForward);
            } else {
                moves.add(new ChessMove(start, oneForward, null));
            }

            // Try double move
            if (startRow == doubleMoveRow) {
                int twoForwardRow = startRow + 2 * direction;
                ChessPosition twoForward = new ChessPosition(twoForwardRow, col);
                if (board.getPiece(twoForward) == null) {
                    moves.add(new ChessMove(start, twoForward, null));
                }
            }
        }
    }

    private void diagonalMoves(Collection<ChessMove> moves, ChessBoard board,
                                      ChessPosition start, int newRow, int newCol,
                                      ChessGame.TeamColor color, int promotionRow) {

        if (newCol < 1 || newCol > 8) {return;}

        ChessPosition end = new ChessPosition(newRow, newCol);
        ChessPiece target = board.getPiece(end);

        if (target != null && target.getTeamColor() != color) {
            if (newRow == promotionRow) {
                addPromotionMoves(moves, start, end);
            } else {
                moves.add(new ChessMove(start, end, null));
            }
        }
    }
}
