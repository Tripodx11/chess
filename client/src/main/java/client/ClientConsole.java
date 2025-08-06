package client;

import chess.*;
import model.AuthData;
import model.GameData;
import results.CreateGameResult;
import results.ListGamesResult;
import ui.BoardRenderer;
import websocket.ServerMessageObserver;

import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.*;

import static ui.EscapeSequences.*;

public class ClientConsole implements ServerMessageObserver {

    private boolean loggedIn = false;
    private String authToken = null;
    private ServerFacade facade;
    private List<GameData> cachedGames = new ArrayList<>();
    private String currentColor;
    private String username = null;

    public ClientConsole() {}

    public void setFacade(ServerFacade facade) {
        this.facade = facade;
    }

    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case LOAD_GAME -> {
                LoadGameMessage load = (LoadGameMessage) message;
                int gameID = load.getGameID();
                ChessGame game = load.getGame();

                // Update cached game
                for (GameData g : cachedGames) {
                    if (g.getGameID() == gameID) {
                        g.setGame(game);
                        break;
                    }
                }
                System.out.println(); // Single clean line before board
                BoardRenderer.drawBoard(game, currentColor, null, null);
                System.out.println(); // Space after board
                System.out.print("[GAMEPLAY_MODE] >>> ");
            }

            case NOTIFICATION -> {
                NotificationMessage note = (NotificationMessage) message;
                String msg = note.getMessage();
                System.out.println();
                System.out.println("NOTIFICATION: " + msg);
                System.out.print("[GAMEPLAY_MODE] >>> ");
            }

            case ERROR -> {
                ErrorMessage err = (ErrorMessage) message;
                //System.out.println();
                System.out.println("SERVER ERROR: " + err.getErrorMessage());
                System.out.print("[GAMEPLAY_MODE] >>> ");
            }
        }

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
            this.username = authData.getUsername();
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
            this.username = authData.getUsername();
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
            this.currentColor = color;
            gameplayMode(Integer.parseInt(input[1]), color, false);
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
            this.currentColor = "white";
            gameplayMode(Integer.parseInt(input[1]), currentColor, true);
        } catch (NumberFormatException e) {
            System.out.println("Invalid Game ID: must be a number");
        } catch (Exception e) {
            System.out.println("Observe game failed");
        }
    }

    private void gameplayMode(int gameID, String color, boolean isObserver) {
        // websocket, commend, notifications, loop for game play, etc.
        facade.connectToGame(authToken, gameID, isObserver);
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        Scanner scanner = new Scanner(System.in);
        //System.out.print("[GAMEPLAY_MODE] >>> ");
        while (true) {
//            System.out.print("[GAMEPLAY_MODE] >>> ");
            String input = scanner.nextLine().trim();
            String[] inputList = input.split("\\s+");
            switch (inputList[0]) {
                case "help" -> {
                    gameplayHelpHelper(inputList);
                    System.out.print("[GAMEPLAY_MODE] >>> ");
                }
                case "redraw" -> {
                    BoardRenderer.drawBoard(cachedGames.get(gameID-1).getGame(), color, null, null);
                    System.out.print("[GAMEPLAY_MODE] >>> ");
                }
                case "move" -> gameplayMakeMoveHelper(inputList, gameID);
                case "highlight" -> {
                    highlightHelper(inputList, gameID);
                    System.out.print("[GAMEPLAY_MODE] >>> ");
                }
                case "resign" -> {
                    resignHelper(inputList, gameID);
                    System.out.print("[GAMEPLAY_MODE] >>> ");
                }
                case "leave" -> {
                    int num = leaveHelper(inputList, gameID);
                    if (num==2) {
                        return;
                    };
                }
                default -> {
                    System.out.println("Unknown command");
                    System.out.print("[GAMEPLAY_MODE] >>> ");
                }
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
        System.out.println(SET_TEXT_COLOR_BLUE + "  move <start position> <end position>" + RESET_TEXT_COLOR + " - to move a piece");
        System.out.println(SET_TEXT_COLOR_YELLOW + "  highlight <position>" + RESET_TEXT_COLOR + " - of a piece");
        System.out.println(SET_TEXT_COLOR_MAGENTA + "  resign" + RESET_TEXT_COLOR + " - to forfeit");
        System.out.println(SET_TEXT_COLOR_RED + "  leave" + RESET_TEXT_COLOR + " - game");
        System.out.println();
    }

    private void gameplayMakeMoveHelper(String[] input, int gameID) {
        ChessPiece.PieceType promotionPiece = null;

        if (input.length == 4) {
            promotionPiece = parsePromotion(input[3]);
        } else if (input.length != 3) {
            System.out.println("Did not meet usage form: move <start position> <end position> (ex: move a2 a4)");
            System.out.print("[GAMEPLAY_MODE] >>> ");
            return;
        }

        try {
            ChessPosition startPos = parsePosition(input[1]);
            ChessPosition endPos = parsePosition(input[2]);
            ChessMove move = new ChessMove(startPos, endPos, promotionPiece);
            facade.makeMove(authToken, gameID, move);

        } catch (Exception e) {
            System.out.println("Invalid move format. Use positions like 'e2 e4'");
            System.out.print("[GAMEPLAY_MODE] >>> ");
        }
    }

    private ChessPiece.PieceType parsePromotion(String input) {
        switch (input.toLowerCase()) {
            case "queen" -> {
                return ChessPiece.PieceType.QUEEN;
            }
            case "rook" -> {
                return ChessPiece.PieceType.ROOK;
            }
            case "bishop" -> {
                return ChessPiece.PieceType.BISHOP;
            }
            case "knight" -> {
                return ChessPiece.PieceType.KNIGHT;
            }
            default -> throw new IllegalArgumentException("Invalid promotion piece: " + input);
        }
    }

    private ChessPosition parsePosition(String input) throws IllegalArgumentException {

        char file = input.charAt(0);
        int row = Character.getNumericValue(input.charAt(1));

        if (file < 'a' || file > 'h' || row < 1 || row > 8) {
            throw new IllegalArgumentException();
        }

        int col = file - 'a' + 1;
        return new ChessPosition(row, col);
    }

    private void resignHelper(String[] input, int gameID) {
        if (input.length != 1) {
            System.out.println("Did not meet usage form: resign");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you sure you want to resign? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("yes")) {
            System.out.println("Resignation cancelled.");
            return;
        }

        try {
            facade.resign(authToken, gameID);
//            System.out.println("You have resigned the game.");
        } catch (Exception e) {
            System.out.println("Resign failed: " + e.getMessage());
        }
    }

    private int leaveHelper(String[] input, int gameID) {
        if (input.length != 1) {
            System.out.println("Did not meet usage form: leave");
            System.out.print("[GAMEPLAY_MODE] >>> ");
            return 0;
        }

        facade.leave(authToken, gameID);
        System.out.println("You have left the game.");

        return 2;

    }

    public void highlightHelper(String[] input, int gameID) {
        if (input.length != 2) {
            System.out.println("Did not meet usage form: highlight <position>");
            System.out.print("[GAMEPLAY_MODE] >>> ");
            return;
        }

        try {
            ChessPosition start = parsePosition(input[1]);
            ChessGame game = cachedGames.get(gameID - 1).getGame();

            List<ChessMove> validMoves = (List<ChessMove>) game.validMoves(start);
            Set<ChessPosition> highlightSquares = new HashSet<>();
            for (ChessMove move : validMoves) {
                highlightSquares.add(move.getEndPosition());
            }

            BoardRenderer.drawBoard(game, currentColor, highlightSquares, start); // new overloaded method

        } catch (Exception e) {
            System.out.println("Invalid input or position. Use like: highlight e2");
            System.out.print("[GAMEPLAY_MODE] >>> ");
        }
    }
}
