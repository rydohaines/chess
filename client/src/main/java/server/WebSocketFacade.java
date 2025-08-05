package server;

import com.google.gson.Gson;
import repls.Repl;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint {
    Repl notificationHandler;
    Session session;
    public WebSocketFacade(String url, Repl notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
    public void connect(String authToken,int gameID) throws Exception{
        try{
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT,authToken,gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch(Exception ex){
            throw new Exception("Unable to connect");
        }
    }

}
