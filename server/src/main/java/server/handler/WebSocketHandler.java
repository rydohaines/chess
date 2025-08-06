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
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
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
            case RESIGN -> resign(command.getAuthToken(),command.getGameID(),session);
        }
    }
    public void resign(String authToken,int gameID, Session session) throws Exception {
        String user = null;
        var errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        try{
            user = userService.getAuthDataAccess().getUser(authToken);
        } catch(Exception ex){
            errorMessage.setErrorMessage("Unauthorized");
            throw new Exception(ex.getMessage());
        }
        GameData game = gameService.getGame(gameID);
        if(game.game().isGameComplete()){
            errorMessage.setErrorMessage("Game is over cannot resign");
            connections.notify(user,errorMessage,gameID,session);
            return;
        }
        if(!Objects.equals(user, game.blackUsername()) && !Objects.equals(user, game.whiteUsername())){
            errorMessage.setErrorMessage("Observer cannot resign");
            connections.notify(user,errorMessage,gameID,session);
            return;
        }
        game.game().completeGame();
        gameService.updateBoard(gameID,game.game());
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.setMessage(String.format("%s has resigned",user));
        connections.notify(user,notification,gameID,session);
        connections.broadcast(user,notification,gameID);
    }
    public void makeMove(String authToken, int gameID, ChessMove move, Session session) throws Exception {
        String user;
        try {
             user = userService.getAuthDataAccess().getUser(authToken);
        }catch (Exception ex){
            var errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage("Unauthorized");
            throw new Exception(ex.getMessage());
        }
        ChessGame.TeamColor teamColor;
        try {
            teamColor = gameService.getTeamColor(user, gameID);
        }catch (Exception ex){
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            error.setErrorMessage("Observers cannot make moves");
            connections.notify(user,error,gameID,session);
            return;
        }
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
            error.setErrorMessage("Not your turn move failed");
            connections.notify(user,error,gameID,session);
            return;
        }
        try{
            chessGame.makeMove(move);
        }catch (Exception ex){
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            error.setErrorMessage("Invalid Move");
            connections.notify(user,error,gameID,session);
            return;
        }
        if(chessGame.isGameComplete()){
            var error = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            error.setErrorMessage("Game is complete cannot move");
            connections.notify(user,error,gameID,session);
            return;
        }
        gameService.updateBoard(gameID,chessGame);
        GameData gameData = gameService.getGame(gameID);
        var gameNotification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        gameNotification.updateGame(gameData);
        connections.notify(user,gameNotification,gameID,session);
        connections.broadcast(user,gameNotification,gameID);
        var moveMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        moveMessage.setMessage(move.toString());
        connections.broadcast(user,moveMessage,gameID);
        if(chessGame.isInCheckmate(opponentColor)){
           var message = String.format("%s is in checkmate",opponentColor);
           moveMessage.setMessage(message);
           connections.broadcast(user,moveMessage,gameID);
           connections.notify(user,moveMessage,gameID,session);
        }
        else if(chessGame.isInStalemate(opponentColor)){
            moveMessage.setMessage("Game is in Stalemate");
            connections.notify(user,moveMessage,gameID,session);
            connections.broadcast(user,moveMessage,gameID);
        }
        else if(chessGame.isInCheck(opponentColor)){
            var checkMessage = String.format("%s is in check",opponentColor);
            moveMessage.setMessage(checkMessage);
            connections.notify(user,moveMessage,gameID,session);
            connections.broadcast(user,moveMessage,gameID);
        }

    }
    public void leave(String authToken, int gameID, Session session) throws Exception{
        String user = userService.getAuthDataAccess().getUser(authToken);
        gameService.removeUser(user,gameID);
        connections.remove(user,session,gameID);
        var message = String.format("%s has left the game",user);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.setMessage(message);
        connections.broadcast(user,notification,gameID);
    }
    public void connect(String authToken, int gameID, Session session) throws Exception {
        String user = null;
        try {
            user = userService.getAuthDataAccess().getUser(authToken);
        } catch (Exception ex){
            var message = "Unauthorized";
            var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            notification.setErrorMessage(message);
            session.getRemote().sendString(new Gson().toJson(notification));
            return;
        }
        connections.add(user,session,gameID);
        GameData game;
        try {
            game = gameService.getGame(gameID);
        } catch (Exception ex){
            var message = "Invalid GameID";
            var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            notification.setErrorMessage(message);
            session.getRemote().sendString(new Gson().toJson(notification));
            return;
        }
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
        connections.notify(user,gameNotification,gameID,session);
        connections.broadcast(user,notification,gameID);

    }
    @OnWebSocketError
    public void onError(Session session, Throwable error) throws IOException {
        if(error.getMessage() != null) {
            var errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            errorMessage.setErrorMessage(error.getMessage());
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        }
    }
}
