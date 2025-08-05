package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;


public class WebSocketClientEndpoint extends WebSocketAdapter {

    private final Gson gson = new Gson();
    private final ServerMessageObserver observer;

    public WebSocketClientEndpoint(ServerMessageObserver observer) {
        this.observer = observer;
    }

    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
//        System.out.println("WebSocket connected");
    }

    public void onWebSocketText(String message) {
        super.onWebSocketText(message);

        try {
            // First: extract the message type only
            ServerMessage temp = gson.fromJson(message, ServerMessage.class);
            ServerMessage.ServerMessageType type = temp.getServerMessageType();

            ServerMessage fullMessage = switch (type) {
                case LOAD_GAME -> gson.fromJson(message, LoadGameMessage.class);
                case NOTIFICATION -> gson.fromJson(message, NotificationMessage.class);
                case ERROR -> gson.fromJson(message, ErrorMessage.class);
            };

            observer.notify(fullMessage);

        } catch (Exception e) {
            System.err.println("Failed to parse server message: " + e.getMessage());
        }
    }

    public void sendCommand(UserGameCommand command) {
        try {
            String json = gson.toJson(command);
            if (isConnected()) {
                getSession().getRemote().sendString(json);
            } else {
                System.err.println("WebSocket is not connected.");
            }
        } catch (Exception e) {
            System.err.println("Failed to send command: " + e.getMessage());
        }
    }

}
