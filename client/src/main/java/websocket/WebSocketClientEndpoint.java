package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;


public class WebSocketClientEndpoint extends WebSocketAdapter {

    private final Gson gson = new Gson();
    private final ServerMessageObserver observer;

    public WebSocketClientEndpoint(ServerMessageObserver observer) {
        this.observer = observer;
    }

    public void onWebSocketConnect(Session session) {
        super.onWebSocketConnect(session);
        System.out.println("WebSocket connected");
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);

        try {
            ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
            observer.notify(serverMessage);
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


//public void sendCommand(UserGameCommand command) {
//    if (session != null && session.isOpen()) {
//        String json = gson.toJson(command);
//        session.getAsyncRemote().sendText(json);
//    } else {
//        System.err.println("WebSocket session not open");
//    }
//}