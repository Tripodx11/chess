package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class ChessGame {

    private ChessBoard chessBoard;
    private ChessGame.TeamColor teamTurn;

    public ChessGame() {
        chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        teamTurn = TeamColor.WHITE;

    }

    public TeamColor getTeamTurn() {
        return teamTurn;
    }


    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (chessBoard.getPiece(startPosition) == null) {
            return null;
        }

        Collection<ChessMove> allMoves = chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);
        Collection<ChessMove> legalMoves = new ArrayList<>();

        for (ChessMove move : allMoves) {
            ChessBoard clonedBoard = deepCopyCloneBoard();
            ChessPiece piece = clonedBoard.getPiece(move.getStartPosition());
            clonedBoard.addPiece(move.getEndPosition(), piece);
            clonedBoard.addPiece(move.getStartPosition(), null);

            if (!isInCheck(piece.getTeamColor(), clonedBoard)) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    public ChessBoard deepCopyCloneBoard() {
        ChessBoard copy = new ChessBoard();
        for (int i=1; i<9; i++) {
            for (int j = 1; j < 9; j++) {
                if (chessBoard.getPiece(new ChessPosition(i,j)) != null) {
                    ChessPiece original = chessBoard.getPiece(new ChessPosition(i, j));
                    ChessPiece clonedPiece = new ChessPiece(original.getTeamColor(), original.getPieceType());
                    copy.addPiece(new ChessPosition(i, j), clonedPiece);
                }
            }
        }
        return copy;
    }



    /**
     * Makes a move in a chess game
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        if (validMoves == null || !validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid Move: " + move);
        } else if (chessBoard.getPiece(move.getStartPosition()).getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Not Piece's Turn For Move: " + move);
        }

        if (move.getPromotionPiece() == null) {
            chessBoard.addPiece(move.getEndPosition(), chessBoard.getPiece(move.getStartPosition()));
            chessBoard.addPiece(move.getStartPosition(), null);
            teamTurn = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        } else {
            ChessPiece.PieceType promotion = move.getPromotionPiece();
            ChessPiece piece = new ChessPiece(chessBoard.getPiece(move.getStartPosition()).getTeamColor(), promotion);
            chessBoard.addPiece(move.getEndPosition(), piece);
            chessBoard.addPiece(move.getStartPosition(), null);
            teamTurn = teamTurn == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return checkForCheck(teamColor, this.chessBoard);
    }

    private boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        return checkForCheck(teamColor, board);
    }

    private boolean checkForCheck(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPos = null;

        search:
        for (int i=1; i<9; i++) {
            for (int j=1; j<9; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    kingPos = new ChessPosition(i, j);
                    break search;
                }
            }
        }

        for (int i=1; i<9; i++) {
            for (int j=1; j<9; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece == null || piece.getTeamColor() == teamColor) continue;
                for (ChessMove move : board.getPiece(new ChessPosition(i, j)).pieceMoves(board, new ChessPosition(i, j))) {
                    if (move.getEndPosition().equals(kingPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && noMovesExist(teamColor);
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && noMovesExist(teamColor);
    }

    private boolean noMovesExist(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(i, j));
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(new ChessPosition(i, j));
                    if (moves != null && !moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public void setBoard(ChessBoard board) {
        chessBoard = board;
    }


    public ChessBoard getBoard() {
        return chessBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(chessBoard, chessGame.chessBoard) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, teamTurn);
    }
}
