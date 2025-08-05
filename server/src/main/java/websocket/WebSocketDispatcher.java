package websocket;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.handlers.ConnectHandler;
import websocket.handlers.MoveHandler;

public class WebSocketDispatcher {

    private static DataAccess dataAccess;
    private static ConnectionManager connections;

    public static void initialize(DataAccess dao, ConnectionManager manager) {
        dataAccess = dao;
        connections = manager;
    }

    public static void handle(UserGameCommand command, Session session) throws DataAccessException {
        if (dataAccess == null) {
            System.err.println("WebSocketDispatcher not initialized with DAO");
            return;
        }

        switch (command.getCommandType()) {
            case CONNECT -> new ConnectHandler(dataAccess, connections).handle(command, session);
            case MAKE_MOVE -> {
                MakeMoveCommand moveCommand = (MakeMoveCommand) command;
                new MoveHandler(dataAccess, connections).handle(moveCommand, session);
            }
//            case LEAVE -> new LeaveHandler(dataAccess).handle(command, session);
            case RESIGN -> new ResignHandler(dataAccess).handle(command, session);
            default -> System.err.println("Unknown command type: " + command.getCommandType());
        }
    }
}
