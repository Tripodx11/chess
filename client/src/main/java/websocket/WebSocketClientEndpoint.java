package websocket;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import websocket.commands.UserGameCommand;
import websocket.messages.*;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;


@ClientEndpoint
public class WebSocketClientEndpoint {

    private final Gson gson = new Gson();
    private final ServerMessageObserver observer;
    private Session session;  // javax.websocket.Session

    public WebSocketClientEndpoint(ServerMessageObserver observer) {
        this.observer = observer;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
//        System.out.println("WebSocket connected");
    }

    @OnMessage
    public void onMessage(String message) {
        try {
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
            if (session != null && session.isOpen()) {
                session.getAsyncRemote().sendText(json);  // async send
            } else {
                System.err.println("WebSocket is not connected.");
            }
        } catch (Exception e) {
            System.err.println("Failed to send command: " + e.getMessage());
        }
    }

    // Optional: in case you want to expose the session
    public Session getSession() {
        return session;
    }
}
