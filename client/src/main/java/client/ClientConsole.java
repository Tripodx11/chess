package client;

import model.AuthData;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientConsole {

    private boolean loggedIn = false;
    private String authToken = null;
    private final ServerFacade facade;

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
                    case "join" -> joinHelper(inputList);
                    case "observe" -> observeHelper(inputList);
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

    }

    private void logoutHelper() {
        loggedIn = false;
    }

    private void createHelper(String[] input) {

    }

    private void joinHelper(String[] input) {

    }

    private void observeHelper(String[] input) {

    }

}
