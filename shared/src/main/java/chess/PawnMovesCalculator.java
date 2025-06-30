package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements  PieceMoveCalculator{

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color) {

        Collection<ChessMove> validMoves = new ArrayList<>();
        int startRow = position.getRow();
        int startCol = position.getColumn();
        int newRow = startRow +1;
        int newColLeft = startCol - 1;
        int newColRight = startCol + 1;

        if (color == ChessGame.TeamColor.WHITE) {

            if (startRow < 8) {
                ChessPosition newMovePosition = new ChessPosition(newRow, startCol);
                ChessPiece newMovePiece = board.getPiece(newMovePosition);

                //normal move going forward
                if (newMovePiece == null) {
                    if (newRow == 8) {
                        validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.KNIGHT));
                        validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.BISHOP));
                    } else {
                        ChessMove move = new ChessMove(position, newMovePosition, null);
                        validMoves.add(move);
                    }
                }

                //double move going forward
                if (newMovePiece == null && startRow == 2) {
                    int twoMoveNewRow = startRow + 2;
                    ChessPosition twoMoveNewMovePosition = new ChessPosition(twoMoveNewRow, startCol);
                    ChessPiece twoMoveNewMovePiece = board.getPiece(twoMoveNewMovePosition);
                    if (twoMoveNewMovePiece == null) {
                        ChessMove move = new ChessMove(position, twoMoveNewMovePosition, null);
                        validMoves.add(move);
                    }
                }

                //diagonal up left
                if (startCol > 1) {
                    newMovePosition = new ChessPosition(newRow, newColLeft);
                    newMovePiece = board.getPiece(newMovePosition);
                    if (newMovePiece != null && newMovePiece.getTeamColor() != color) {
                        if (newRow == 8) {
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.ROOK));
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.BISHOP));
                        } else {
                            ChessMove move = new ChessMove(position, newMovePosition, null);
                            validMoves.add(move);
                        }
                    }
                }

                //diagonal up right
                if (startCol < 8) {
                    newMovePosition = new ChessPosition(newRow, newColRight);
                    newMovePiece = board.getPiece(newMovePosition);
                    if (newMovePiece != null && newMovePiece.getTeamColor() != color) {
                        if (newRow == 8) {
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.ROOK));
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.BISHOP));
                        } else {
                            ChessMove move = new ChessMove(position, newMovePosition, null);
                            validMoves.add(move);
                        }
                    }
                }

            }
        }


        if (color == ChessGame.TeamColor.BLACK) {
            newRow = startRow - 1;

            if (startRow > 1) {
                ChessPosition newMovePosition = new ChessPosition(newRow, startCol);
                ChessPiece newMovePiece = board.getPiece(newMovePosition);

                //normal move going down
                if (newMovePiece == null) {
                    if (newRow == 1) {
                        validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.KNIGHT));
                        validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.BISHOP));
                    } else {
                        ChessMove move = new ChessMove(position, newMovePosition, null);
                        validMoves.add(move);
                    }
                }

                //double move going down
                if (newMovePiece == null && startRow == 7) {
                    int twoMoveNewRow = startRow - 2;
                    ChessPosition twoMoveNewMovePosition = new ChessPosition(twoMoveNewRow, startCol);
                    ChessPiece twoMoveNewMovePiece = board.getPiece(twoMoveNewMovePosition);
                    if (twoMoveNewMovePiece == null) {
                        ChessMove move = new ChessMove(position, twoMoveNewMovePosition, null);
                        validMoves.add(move);
                    }
                }

                //diagonal down left
                if (startCol > 1) {
                    newMovePosition = new ChessPosition(newRow, newColLeft);
                    newMovePiece = board.getPiece(newMovePosition);
                    if (newMovePiece != null && newMovePiece.getTeamColor() != color) {
                        if (newRow == 1) {
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.ROOK));
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.BISHOP));
                        } else {
                            ChessMove move = new ChessMove(position, newMovePosition, null);
                            validMoves.add(move);
                        }
                    }
                }

                //diagonal down right
                if (startCol < 8) {
                    newMovePosition = new ChessPosition(newRow, newColRight);
                    newMovePiece = board.getPiece(newMovePosition);
                    if (newMovePiece != null && newMovePiece.getTeamColor() != color) {
                        if (newRow == 1) {
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.QUEEN));
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.ROOK));
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.KNIGHT));
                            validMoves.add(new ChessMove(position, newMovePosition, ChessPiece.PieceType.BISHOP));
                        } else {
                            ChessMove move = new ChessMove(position, newMovePosition, null);
                            validMoves.add(move);
                        }

                    }
                }
            }
        }

        return validMoves;
    }

}
