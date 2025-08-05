package websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

@WebSocket
public class WebSocketHandler {

    private static final Gson gson = new Gson();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("WebSocket opened: " + session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("Received WebSocket message: " + message);
        try {
            JsonObject obj = JsonParser.parseString(message).getAsJsonObject();
            String type = obj.get("commandType").getAsString();

            UserGameCommand command;

            if (type.equals("MAKE_MOVE")) {
                command = gson.fromJson(message, MakeMoveCommand.class);
            } else {
                command = gson.fromJson(message, UserGameCommand.class);  // catch CONNECT, RESIGN, LEAVE here
            }

            WebSocketDispatcher.handle(command, session);

        } catch (Exception e) {
            System.err.println("Error handling WebSocket message: " + e.getMessage());
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("WebSocket closed: " + session + " Reason: " + reason);
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) {
        System.err.println("WebSocket error: " + error.getMessage());
    }
}
