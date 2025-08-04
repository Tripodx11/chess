package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import results.CreateGameResult;
import results.ListGamesResult;
import websocket.ServerMessageObserver;
import websocket.messages.ServerMessage;

import java.util.*;

import static ui.EscapeSequences.*;

public class ClientConsole implements ServerMessageObserver {

    private boolean loggedIn = false;
    private String authToken = null;
    private ServerFacade facade;
    private List<GameData> cachedGames = new ArrayList<>();

    public ClientConsole() {}

    public void setFacade(ServerFacade facade) {
        this.facade = facade;
    }

    public void notify(ServerMessage message) {

    }

    public void run() {
        System.out.print("Welcome to chess! Type \"help\" to get started. \n" );
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print(loggedIn ? "[LOGGED_IN] >>> " : "[LOGGED_OUT] >>> ");
            String input = scanner.nextLine().trim();
            String[] inputList = input.split("\\s+");

            if (!loggedIn) {
                switch (inputList[0]) {
                    case "help" -> loggedOutHelpHelper(inputList);
                    case "register" -> registerHelper(inputList);
                    case "login" -> loginHelper(inputList);
                    case "quit" -> System.exit(0);
                    default -> System.out.println("Unknown command");
                }
            } else {
                switch (inputList[0]) {
                    case "help" -> loggedInHelpHelper(inputList);
                    case "logout" -> logoutHelper(inputList);
                    case "create" -> createHelper(inputList);
                    case "list" -> listHelper(inputList);
                    case "join" -> joinHelper(inputList);
                    case "observe" -> observeHelper(inputList);
                    case "quit" -> System.exit(0);
                    default -> System.out.println("Unknown command");
                }
            }
        }

    }

    private void loggedOutHelpHelper(String[] input) {

        if (input.length != 1) {
            System.out.println("Did not meet usage form: help");
            return;
        }

        System.out.println();
        System.out.println(SET_TEXT_COLOR_GREEN + "  register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR + " - to create an account");
        System.out.println(SET_TEXT_COLOR_BLUE + "  login <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR + " - to play chess");
        System.out.println(SET_TEXT_COLOR_YELLOW + "  quit" + RESET_TEXT_COLOR + " - playing chess");
        System.out.println(SET_TEXT_COLOR_MAGENTA + "  help" + RESET_TEXT_COLOR + " - with possible commands");
        System.out.println();
    }

    private void registerHelper(String[] input) {

        if (input.length != 4) {
            System.out.println("Did not meet usage form: register <USERNAME> <PASSWORD> <EMAIL>");
            return;
        }

        try {
            AuthData authData = facade.register(input[1], input[2], input[3]);
            this.authToken = authData.getAuthToken();
            loggedIn = true;
            System.out.println("Registration successful. You are now logged in.");
        } catch (Exception e) {
            if (e.getMessage().contains("403")) {
                System.out.println("Username already taken");
            } else {
                System.out.println("Registration failed");
            }
        }
    }

    private void loginHelper(String[] input) {

        if (input.length != 3) {
            System.out.println("Did not meet usage form: login <USERNAME> <PASSWORD>");
            return;
        }

        try {
            AuthData authData = facade.login(input[1], input[2]);
            this.authToken = authData.getAuthToken();
            loggedIn = true;
            System.out.println("Login successful. You are now logged in.");
        } catch (Exception e) {
            if (e.getMessage().contains("401")) {
                System.out.println("Invalid credentials");
            } else {
                System.out.println("Registration failed");
            }
        }

    }

    private void loggedInHelpHelper(String[] input) {
        if (input.length != 1) {
            System.out.println("Did not meet usage form: help");
            return;
        }
        System.out.println();
        System.out.println(SET_TEXT_COLOR_GREEN + "  help" + RESET_TEXT_COLOR + " - with possible commands");
        System.out.println(SET_TEXT_COLOR_RED + "  logout" + RESET_TEXT_COLOR + " - when you are done");
        System.out.println(SET_TEXT_COLOR_YELLOW + "  create <NAME>" + RESET_TEXT_COLOR + " - a game");
        System.out.println(SET_TEXT_COLOR_WHITE + "  list" + RESET_TEXT_COLOR + " - games");
        System.out.println(SET_TEXT_COLOR_MAGENTA + "  join <ID> [WHITE|BLACK]" + RESET_TEXT_COLOR + " - a game");
        System.out.println(SET_TEXT_COLOR_BLUE + "  observe <ID>" + RESET_TEXT_COLOR + " - a game");
        System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "  quit" + RESET_TEXT_COLOR + " - playing chess");
        System.out.println();
    }

    private void logoutHelper(String[] input) {
        if (input.length != 1) {
            System.out.println("Did not meet usage form: logout");
            return;
        }
        loggedIn = false;
    }

    private void createHelper(String[] input) {
        if (input.length != 2) {
            System.out.println("Did not meet usage form: create <NAME>");
            return;
        }

        try {
            CreateGameResult createGameResult = facade.create(authToken, input[1]);
            ListGamesResult listGamesResult = facade.list(authToken);
            cachedGames = listGamesResult.getGames();
            System.out.println("Game creation successful. Game list updated.");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void listHelper(String[] input) {
        if (input.length != 1) {
            System.out.println("Did not meet usage form: list");
            return;
        }

        try {
            ListGamesResult listGamesResult = facade.list(authToken);
            cachedGames = listGamesResult.getGames();

            System.out.println("Current games");
            for (int i = 0; i < cachedGames.size(); i++) {
                GameData game = cachedGames.get(i);
                String name = game.getGameName();
                String whiteUser = game.getWhiteUsername() != null ? game.getWhiteUsername() : "[empty]";
                String blackUser = game.getBlackUsername() != null ? game.getBlackUsername() : "[empty]";
                System.out.printf("  %d: Game Name: %s, White User: %s, Black User: %s \n",
                        i+1, name, whiteUser, blackUser);
            }
        } catch (Exception e) {
            System.out.println("Failed to list games: " + e.getMessage());
        }

    }

    private void joinHelper(String[] input) {
        if (input.length != 3) {
            System.out.println("Did not meet usage form: join <ID> [WHITE|BLACK]");
            return;
        }

        if (!input[2].toLowerCase().trim().equals("white") && !input[2].toLowerCase().trim().equals("black")) {
            System.out.println("Invalid color");
            return;
        }

        try {
            int inputID = Integer.parseInt(input[1]) - 1;
            if (inputID < 0 || inputID >= cachedGames.size()) {
                System.out.println("Invalid Game ID");
                return;
            }
            int sysID = cachedGames.get(inputID).getGameID();
            String color = input[2].toLowerCase(Locale.ROOT).trim();

            facade.join(authToken, color, sysID);
            System.out.println("Join game successful");
            drawBoard(cachedGames.get(inputID).getGame(), color);
            gameplayMode(inputID, color, false);
        } catch (NumberFormatException e) {
            System.out.println("Invalid Game ID: must be a number");
        } catch (Exception e) {
            if (e.getMessage().contains("403")) {
                System.out.println("Color already taken");
            }
            System.out.println("Join game failed");
        }
    }

    private void observeHelper(String[] input) {
        if (input.length != 2) {
            System.out.println("Did not meet usage form: observe <ID>");
            return;
        }

        try {
            int inputID = Integer.parseInt(input[1]) - 1;
            if (inputID < 0 || inputID >= cachedGames.size()) {
                System.out.println("Invalid Game ID");
                return;
            }
            drawBoard(cachedGames.get(inputID).getGame(), "white");
            gameplayMode(inputID, "white", true);
        } catch (NumberFormatException e) {
            System.out.println("Invalid Game ID: must be a number");
        } catch (Exception e) {
            System.out.println("Observe game failed");
        }
    }

    public void drawBoard(ChessGame game, String color) {
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
                boolean isDark = (row + col) % 2 == 0;
                String bgColor = isDark ? SET_BG_COLOR_DARK_BROWN : SET_BG_COLOR_MEDIUM_BROWN;

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

    private String getPieceSymbol(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> BLACK_KING;
            case QUEEN -> BLACK_QUEEN;
            case ROOK -> BLACK_ROOK;
            case BISHOP -> BLACK_BISHOP;
            case KNIGHT -> BLACK_KNIGHT;
            case PAWN -> BLACK_PAWN;
        };
    }

    private void gameplayMode(int gameID, String color, boolean isObserver) {
        // websocket, commend, notifications, loop for game play, etc.
        //connect to websocket
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("[GAMEPLAY_MODE] >>> ");
            String input = scanner.nextLine().trim();
            String[] inputList = input.split("\\s+");
            switch (inputList[0]) {
                case "help" -> gameplayHelpHelper(inputList);
                case "redraw" -> drawBoard(cachedGames.get(gameID).getGame(), color);
                //case "make move" -> loginHelper(inputList);
                //case "show moves" -> loginHelper(inputList);
                //case "resign" -> System.exit(0);
                case "leave" -> {return;}
                default -> System.out.println("Unknown command");
            }
        }
    }

    private void gameplayHelpHelper(String[] input) {

        if (input.length != 1) {
            System.out.println("Did not meet usage form: help");
            return;
        }

        System.out.println();
        System.out.println(SET_TEXT_COLOR_GREEN + "  redraw " + RESET_TEXT_COLOR + " - to see current board");
        System.out.println(SET_TEXT_COLOR_BLUE + "  make move <start position> <end position>" + RESET_TEXT_COLOR + " - to move a piece");
        System.out.println(SET_TEXT_COLOR_YELLOW + "  show moves <position>" + RESET_TEXT_COLOR + " - of a piece");
        System.out.println(SET_TEXT_COLOR_MAGENTA + "  resign" + RESET_TEXT_COLOR + " - to forfeit");
        System.out.println(SET_TEXT_COLOR_RED + "  leave" + RESET_TEXT_COLOR + " - game");
        System.out.println();
    }
}
