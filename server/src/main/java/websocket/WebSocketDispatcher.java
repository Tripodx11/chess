package websocket;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;

public class WebSocketDispatcher {

    private static DataAccess dataAccess;

    public static void initialize(DataAccess dao) {
        dataAccess = dao;
    }

    public static void handle(UserGameCommand command, Session session) {
        if (dataAccess == null) {
            System.err.println("WebSocketDispatcher not initialized with DAO");
            return;
        }

        switch (command.getCommandType()) {
            case CONNECT -> new ConnectHandler(dataAccess).handle(command, session);
            case MAKE_MOVE -> new MoveHandler(dataAccess).handle(command, session);
            case LEAVE -> new LeaveHandler(dataAccess).handle(command, session);
            case RESIGN -> new ResignHandler(dataAccess).handle(command, session);
            default -> System.err.println("Unknown command type: " + command.getCommandType());
        }
    }
}
