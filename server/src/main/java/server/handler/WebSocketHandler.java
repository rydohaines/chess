package server.handler;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
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
import java.sql.SQLException;
import java.util.Objects;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

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
            case MAKE_MOVE -> makeMove(command.getAuthToken(),command.getGameID(),command.getMove(),session);
        }
    }
    public void makeMove(String authToken, int gameID, ChessMove move, Session session) throws Exception {
        String user = userService.getAuthDataAccess().getUser(authToken);
        ChessGame.TeamColor teamColor = gameService.getTeamColor(user,gameID);
        ChessGame.TeamColor opponentColor;
        if(teamColor == WHITE){
            opponentColor = BLACK;
        }
        else{
            opponentColor= WHITE;
        }
        ChessGame chessGame = gameService.getGame(gameID).game();
        if(gameService.getGame(gameID).game().getTeamTurn() != teamColor){
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            error.setMessage("Not your turn move failed");
            connections.notify(user,error);
            return;
        }
        try{
            chessGame.makeMove(move);
        }catch (Exception ex){
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            error.setMessage("Invalid Move");
            connections.notify(user,error);
            return;
        }
        gameService.updateBoard(gameID,chessGame);
        GameData gameData = gameService.getGame(gameID);
        var gameNotification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        gameNotification.updateGame(gameData);
        connections.notify(user,gameNotification);
        connections.broadcast(user,gameNotification);
        var moveMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        moveMessage.setMessage(move.toString());
        connections.broadcast(user,moveMessage);
        if(chessGame.isInCheckmate(opponentColor)){
           var message = String.format("%s is in checkmate",opponentColor);
           moveMessage.setMessage(message);
           connections.broadcast(user,moveMessage);
           connections.notify(user,moveMessage);
        }
        else if(chessGame.isInStalemate(opponentColor)){
            moveMessage.setMessage("Game is in Stalemate");
            connections.notify(user,moveMessage);
            connections.broadcast(user,moveMessage);
        }
        else if(chessGame.isInCheck(opponentColor)){
            var checkMessage = String.format("%s is in check",opponentColor);
            moveMessage.setMessage(checkMessage);
            connections.notify(user,moveMessage);
            connections.broadcast(user,moveMessage);
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
        var gameNotification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        gameNotification.updateGame(game);
        connections.notify(user,gameNotification);
        connections.broadcast(user,notification);

    }
}
