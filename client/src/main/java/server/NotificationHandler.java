package server;

import websocket.messages.ServerMessage;

public interface NotificationHandler {
    public void notify(ServerMessage notification);
}
