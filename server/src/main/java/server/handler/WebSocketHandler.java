package server.handler;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.util.Objects;

@WebSocket
public class WebSocketHandler{
    private final ConnectionManager connections = new ConnectionManager();
    private final UserService userService;
    private final GameService gameService;

    public WebSocketHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), session);
            case LEAVE -> leave(command.getAuthToken(),command.getGameID(),session);
        }
    }
    public void leave(String authToken, int gameID, Session session) throws Exception{
        String user = userService.getAuthDataAccess().getUser(authToken);
        gameService.removeUser(user,gameID);
        connections.remove(user,session);
        var message = String.format("%s has left the game",user);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.setMessage(message);
        connections.broadcast(user,notification);
    }
    public void connect(String authToken, int gameID, Session session) throws Exception{
        String user = userService.getAuthDataAccess().getUser(authToken);
        connections.add(user,session);
        GameData game = gameService.getGame(gameID);
        String color = "observer";
        if(Objects.equals(game.blackUsername(), user)){
            color = "black";
        }else if(Objects.equals(game.whiteUsername(), user)){
            color = "white";
        }
        var message = String.format("%s has entered the game as %s" , user,color);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.setMessage(message);
        connections.broadcast(user,notification);

    }
}
