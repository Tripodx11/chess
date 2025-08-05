package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    private final Map<Integer, Map<String, Session>> gameConnections = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    public void add(int gameID, Session session, String username) {
        gameConnections.putIfAbsent(gameID, new ConcurrentHashMap<>());
        gameConnections.get(gameID).put(username, session);
    }

    public void sendToSession(Session session, ServerMessage message) {
        if (session != null && session.isOpen()) {
            try {
                String json = gson.toJson(message);
                session.getRemote().sendString(json);
            } catch (Exception e) {
                System.err.println("Failed to send message: " + e.getMessage());
            }
        }
    }

    public void broadcast(int gameID, ServerMessage message, Session excludeSession) {
        Map<String, Session> sessionMap = gameConnections.get(gameID);

        if (sessionMap != null) {
            for (Session session : sessionMap.values()) {
                if (!session.equals(excludeSession) && session.isOpen()) {
                    sendToSession(session, message);
                }
            }
        }
    }

    public void remove(int gameID, String username) {
        Map<String, Session> players = gameConnections.get(gameID);
        if (players != null) {
            players.remove(username);
            if (players.isEmpty()) {
                gameConnections.remove(gameID); // Clean up entire game if empty
            }
        }
    }


}