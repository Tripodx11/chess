package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Set;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.BLACK_BISHOP;
import static ui.EscapeSequences.BLACK_KING;
import static ui.EscapeSequences.BLACK_KNIGHT;
import static ui.EscapeSequences.BLACK_PAWN;
import static ui.EscapeSequences.BLACK_QUEEN;
import static ui.EscapeSequences.BLACK_ROOK;
import static ui.EscapeSequences.EMPTY;
import static ui.EscapeSequences.RESET_BG_COLOR;
import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_BG_COLOR_MEDIUM_BROWN;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class BoardRenderer {

    public static void drawBoard(ChessGame game, String color, Set<ChessPosition> highlights, ChessPosition selected) {
        ChessBoard board = game.getBoard();

        int rowStart = color.equals("white") ? 8 : 1;
        int rowEnd = color.equals("white") ? 0 : 9;
        int rowStep = color.equals("white") ? -1 : 1;

        int colStart = color.equals("white") ? 1 : 8;
        int colEnd = color.equals("white") ? 9 : 0;
        int colStep = color.equals("white") ? 1 : -1;

        char fileStart = color.equals("white") ? 'a' : 'h';
        char fileEnd = color.equals("white") ? 'i' : '`';
        int fileStep = color.equals("white") ? 1 : -1;

        int checkerNum = color.equals("white") ? 0 : 1;

        // top file labels
        System.out.print("  " + "\u2003");
        for (char file = fileStart; file != fileEnd; file += (char) fileStep) {
            System.out.print(" " + file + "\u2003");
        }
        System.out.println();

        for (int row = rowStart; row != rowEnd; row += rowStep) {
            System.out.print(" " + row + " ");

            for (int col = colStart; col != colEnd; col+=colStep) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                // Checker pattern
                String bgColor;
                if (highlights != null && highlights.contains(position)) {
                    bgColor = SET_BG_COLOR_GREEN;
                } else if (selected != null && selected.equals(position)) {
                    bgColor = SET_BG_COLOR_YELLOW;
                } else {
                    boolean isDark = (row + col) % 2 == 0;
                    bgColor = isDark ? SET_BG_COLOR_DARK_BROWN : SET_BG_COLOR_MEDIUM_BROWN;
                }

                String symbol = EMPTY;
                if (piece != null) {
                    symbol = getPieceSymbol(piece);
                }

                String fgColor = "";
                if (piece != null) {
                    fgColor = (piece.getTeamColor() == ChessGame.TeamColor.WHITE) ? SET_TEXT_COLOR_WHITE : SET_TEXT_COLOR_BLACK;
                }
                System.out.print(bgColor + fgColor + symbol + RESET_TEXT_COLOR);
            }
            System.out.print(RESET_BG_COLOR);
            System.out.print(" " + row);

            System.out.println(RESET_BG_COLOR); // End row
        }

        // letter labels
        System.out.print("  " + "\u2003");
        for (char file = fileStart; file != fileEnd; file += (char) fileStep) {
            System.out.print( " " + file + "\u2003");
        }
        System.out.println();
    }

    private static String getPieceSymbol(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> BLACK_KING;
            case QUEEN -> BLACK_QUEEN;
            case ROOK -> BLACK_ROOK;
            case BISHOP -> BLACK_BISHOP;
            case KNIGHT -> BLACK_KNIGHT;
            case PAWN -> BLACK_PAWN;
        };
    }
}
