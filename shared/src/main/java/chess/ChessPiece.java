package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        color = pieceColor;
        pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, pieceType);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (pieceType == PieceType.BISHOP) {
            BishopMovesCalculator bishopMoves = new BishopMovesCalculator();
            return bishopMoves.pieceMoves(board, myPosition, color);
        }

        if (pieceType == PieceType.KING) {
            KingMovesCalculator kingMoves = new KingMovesCalculator();
            return kingMoves.pieceMoves(board, myPosition, color);
        }

        if (pieceType == PieceType.KNIGHT) {
            KnightMovesCalculator knightMoves = new KnightMovesCalculator();
            return knightMoves.pieceMoves(board, myPosition, color);
        }

        if (pieceType == PieceType.PAWN) {
            PawnMovesCalculator pawnMoves = new PawnMovesCalculator();
            return pawnMoves.pieceMoves(board, myPosition, color);
        }

        if (pieceType == PieceType.ROOK) {
            RookMovesCalculator rookMoves = new RookMovesCalculator();
            return rookMoves.pieceMoves(board, myPosition, color);
        }

        if (pieceType == PieceType.QUEEN) {
            QueenMovesCalculator queenMoves = new QueenMovesCalculator();
            return queenMoves.pieceMoves(board, myPosition, color);
        }

        return new ArrayList<>();
    }
}
