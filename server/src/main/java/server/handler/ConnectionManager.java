package server.handler;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    private String makeKey(String visitorName, int gameID) {
        return visitorName + ":" + gameID;
    }

    public void add(String visitorName, Session session, int gameID) {
        var connection = new Connection(visitorName, session, gameID);
        connections.put(makeKey(visitorName, gameID), connection);
    }

    public void notify(String username, ServerMessage message, int gameID, Session session) throws Exception {
        var conn = connections.get(makeKey(username, gameID));
        if (conn != null && conn.session.isOpen() && gameID == Objects.requireNonNull(conn).gameID) {
            conn.send(new Gson().toJson(message));
        }
    }

    public void remove(String username, Session session, int gameID) {
        var key = makeKey(username, gameID);
        var conn = connections.get(key);
        if (conn != null && conn.session.equals(session)) {
            connections.remove(key);
        }
    }

    public void broadcast(String excludeVisitorName, ServerMessage serverMessage, int gameID) throws Exception {
        var removeList = new ArrayList<String>();
        for (var entry : connections.entrySet()) {
            var c = entry.getValue();
            if (c.gameID == gameID && c.session.isOpen()) {
                if (!c.visitorName.equals(excludeVisitorName)) {
                    c.send(new Gson().toJson(serverMessage));
                }
            } else {
                removeList.add(entry.getKey());
            }
        }
    }
}