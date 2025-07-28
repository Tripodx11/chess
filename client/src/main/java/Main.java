import chess.*;
import client.ClientConsole;
import client.ServerFacade;
import server.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);

        new Server().run(8080);
        ServerFacade facade = new ServerFacade(8080);

        try {
            facade.clear();
        } catch (IOException e) {
            System.out.println("Failed to clear database: " + e.getMessage());
        }

        ClientConsole client = new ClientConsole(facade);
        client.run();
    }
}