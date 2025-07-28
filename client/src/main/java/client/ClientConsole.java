package client;

import chess.ChessPiece;
import model.AuthData;
import model.GameData;
import service.results.CreateGameResult;
import service.results.ListGamesResult;
import ui.EscapeSequences;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientConsole {

    private boolean loggedIn = false;
    private String authToken = null;
    private final ServerFacade facade;
    private List<GameData> cachedGames = new ArrayList<>();

    public ClientConsole(ServerFacade facade) {
        this.facade = facade;
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
                    case "help" -> loggedOutHelpHelper();
                    case "register" -> registerHelper(inputList);
                    case "login" -> loginHelper(inputList);
                    case "quit" -> System.exit(0);
                    default -> System.out.println("Unknown command");
                }
            } else {
                switch (inputList[0]) {
                    case "help" -> loggedInHelpHelper();
                    case "logout" -> logoutHelper();
                    case "create" -> createHelper(inputList);
                    case "list" -> listHelper();
                    case "join" -> joinHelper(inputList);
                    case "observe" -> observeHelper(inputList);
                    case "quit" -> System.exit(0);
                    default -> System.out.println("Unknown command");
                }
            }
        }

    }

    private void loggedOutHelpHelper() {
        System.out.println();
        System.out.println(SET_TEXT_COLOR_GREEN + "  register <USERNAME> <PASSWORD> <EMAIL>" + RESET_TEXT_COLOR + " - to create an account");
        System.out.println(SET_TEXT_COLOR_BLUE + "  login <USERNAME> <PASSWORD>" + RESET_TEXT_COLOR + " - to play chess");
        System.out.println(SET_TEXT_COLOR_YELLOW + "  quit" + RESET_TEXT_COLOR + " - playing chess");
        System.out.println(SET_TEXT_COLOR_MAGENTA + "  help" + RESET_TEXT_COLOR + " - with possible commands");
        System.out.println();
    }

    private void registerHelper(String[] input) {

        if (input.length != 4) {
            System.out.println("Did not meet usage form: login <USERNAME> <PASSWORD>");
            return;
        }

        try {
            AuthData authData = facade.register(input[1], input[2], input[3]);
            this.authToken = authData.getAuthToken();
            loggedIn = true;
            System.out.println("Registration successful. You are now logged in.");
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void loginHelper(String[] input) {

        if (input.length != 3) {
            System.out.println("Did not meet usage form: register <USERNAME> <PASSWORD> <EMAIL>");
            return;
        }

        try {
            AuthData authData = facade.login(input[1], input[2]);
            this.authToken = authData.getAuthToken();
            loggedIn = true;
            System.out.println("Login successful. You are now logged in.");
        } catch (Exception e) {
            System.out.println("Login failed: " + e.getMessage());
        }

    }

    private void loggedInHelpHelper() {
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

    private void logoutHelper() {
        loggedIn = false;
    }

    private void createHelper(String[] input) {
        if (input.length != 2) {
            System.out.println("Did not meet usage form: create <NAME>");
            return;
        }

        try {
            CreateGameResult createGameResult = facade.create(authToken, input[1]);
            System.out.println("Game creation successful. Game ID: " + createGameResult.getGameID());
        } catch (Exception e) {
            System.out.println("Registration failed: " + e.getMessage());
        }
    }

    private void listHelper() {

        try {
            ListGamesResult listGamesResult = facade.list(authToken);
            cachedGames = listGamesResult.getGames();

            System.out.println("Current games");
            for (int i = 0; i < cachedGames.size(); i++) {
                GameData game = cachedGames.get(i);
                String name = game.getGameName();
                String whiteUser = game.getWhiteUsername() != null ? game.getWhiteUsername() : "[empty]";
                String blackUser = game.getBlackUsername() != null ? game.getBlackUsername() : "[empty]";
                System.out.printf("  %d: Game Name: %s, White User: %s, Black User: %s, System ID: %d%n",
                        i, name, whiteUser, blackUser, game.getGameID());
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

        try {
            int inputID = Integer.parseInt(input[1]);
            if (inputID < 0 || inputID >= cachedGames.size()) {
                System.out.println("Invalid game index.");
                return;
            }
            int sysID = cachedGames.get(inputID).getGameID();

            facade.join(authToken, input[2], sysID);
            System.out.println("Join game successful");
        } catch (Exception e) {
            System.out.println("Join game failed: " + e.getMessage());
        }
    }

    private void observeHelper(String[] input) {
        if (input.length != 2) {
            System.out.println("Did not meet usage form: observe <ID>");
            return;
        }

        try {
            int inputID = Integer.parseInt(input[1]);
            if (inputID < 0 || inputID >= cachedGames.size()) {
                System.out.println("Invalid game index.");
                return;
            }
            int sysID = cachedGames.get(inputID).getGameID();

            facade.join(authToken, null, sysID);
            System.out.println("Observe game successful");
        } catch (Exception e) {
            System.out.println("Observe game failed: " + e.getMessage());
        }
    }

    private String getPieceSymbol(ChessPiece piece) {
        return switch (piece.getTeamColor()) {
            case WHITE -> switch (piece.getPieceType()) {
                case KING -> EscapeSequences.WHITE_KING;
                case QUEEN -> EscapeSequences.WHITE_QUEEN;
                case ROOK -> EscapeSequences.WHITE_ROOK;
                case BISHOP -> EscapeSequences.WHITE_BISHOP;
                case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                case PAWN -> EscapeSequences.WHITE_PAWN;
            };
            case BLACK -> switch (piece.getPieceType()) {
                case KING -> EscapeSequences.BLACK_KING;
                case QUEEN -> EscapeSequences.BLACK_QUEEN;
                case ROOK -> EscapeSequences.BLACK_ROOK;
                case BISHOP -> EscapeSequences.BLACK_BISHOP;
                case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                case PAWN -> EscapeSequences.BLACK_PAWN;
            };
        };
    }


}
