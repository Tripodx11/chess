package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        //Clear board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j< 8; j++) {
                squares[i][j] = null;
            }
        }

        //Set up arrays for looping
        ChessPiece.PieceType[] firstRow = {
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };

        ChessGame.TeamColor[] colors = {ChessGame.TeamColor.WHITE, ChessGame.TeamColor.BLACK};
        int[] pawnRows = {2, 7};
        int[] firstRows = {1, 8};

        for (int i = 0; i < colors.length; i++) {
            ChessGame.TeamColor color = colors[i];

            for (int col = 1; col < 9; col++) {
                ChessPiece newPiece = new ChessPiece(color, ChessPiece.PieceType.PAWN);
                ChessPosition newPosition = new ChessPosition(pawnRows[i], col);
                addPiece(newPosition, newPiece);
            }
            for (int col = 1; col < 9; col++) {
                ChessPiece newPiece = new ChessPiece(color, firstRow[col-1]);
                ChessPosition newPosition = new ChessPosition(firstRows[i], col);
                addPiece(newPosition, newPiece);
            }
        }
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }
}
