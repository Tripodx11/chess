import chess.*;
import client.ClientConsole;
import client.ServerFacade;


import java.io.IOException;

public class Main {

    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);

        ClientConsole client = new ClientConsole();
        ServerFacade facade = new ServerFacade(8080, client);
        client.setFacade(facade);

        try {
            facade.clear();
        } catch (IOException e) {
            System.out.println("Failed to clear database: " + e.getMessage());
        }


        client.run();
    }
}