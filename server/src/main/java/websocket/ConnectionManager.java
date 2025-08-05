package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.*;

public class ConnectionManager {

    private static final Map<Integer, List<Connection>> connections = new HashMap<>();

    public void add(int gameID, Session session, String username) {
        connections.computeIfAbsent(gameID, k -> new ArrayList<>()).add(new Connection(session, username));
    }

    public void remove(Session session) {
        for (List<Connection> connList : connections.values()) {
            connList.removeIf(conn -> conn.session.equals(session));
        }
    }

    public void sendToSession(Session session, ServerMessage message) {
        try {
            session.getRemote().sendString(ServerMessage.serialize(message));
        } catch (IOException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }

    public void broadcast(int gameID, ServerMessage message, Session exclude) {
        var connList = connections.getOrDefault(gameID, Collections.emptyList());
        for (Connection conn : connList) {
            if (!conn.session.equals(exclude)) {
                sendToSession(conn.session, message);
            }
        }
    }

    private record Connection(Session session, String username) {}
}
