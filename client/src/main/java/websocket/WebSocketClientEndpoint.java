package websocket;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;

public class WebSocketClientEndpoint {
    private Session session;
    private final Gson gson = new Gson();
    private final ServerMessageObserver observer;

    public WebSocketClientEndpoint(ServerMessageObserver observer) {
        this.observer = observer;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("WebSocket opened");
    }

    @OnMessage
    public void onMessage(String json) {
        ServerMessage message = gson.fromJson(json, ServerMessage.class);
        observer.notify(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("WebSocket closed: " + reason);
    }

    public void sendCommand(UserGameCommand command) {
        if (session != null && session.isOpen()) {
            String json = gson.toJson(command);
            session.getAsyncRemote().sendText(json);
        } else {
            System.err.println("WebSocket session not open");
        }
    }
}
