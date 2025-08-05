package server;


import com.google.gson.Gson;
import dataaccess.*;
import server.handler.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Map;

public class Server {
    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    {
        try {
            userDataAccess = new MySQLUserDAO();
            authDataAccess = new MySQLAuthDAO();
            gameDataAccess = new MySQLGameDAO();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private final UserService userService = new UserService(userDataAccess,authDataAccess);
    private final GameService gameService = new GameService(gameDataAccess,authDataAccess);
    private final ClearService clearService = new ClearService(userDataAccess,authDataAccess,gameDataAccess);
    private final Handler registerHandler = new RegisterHandler(userService);
    private final Handler clearHandler = new ClearHandler(clearService);
    private final Handler loginHandler = new LoginHandler(userService);
    private final Handler logoutHandler  = new LogoutHandler(userService);
    private final Handler createGameHandler = new CreateGameHandler(gameService);
    private final Handler listGameHandler = new ListGameHandler(gameService);
    private final Handler joinGameHandler = new JoinGameHandler(gameService);
    private final WebSocketHandler webSocketHandler;

    public Server() {
        this.webSocketHandler = new WebSocketHandler(userService,gameService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/ws",webSocketHandler);
        Spark.post("/user", (registerHandler)::handleRequest);
        Spark.delete("/db",(clearHandler)::handleRequest);
        Spark.post("/session",(loginHandler)::handleRequest);
        Spark.delete("/session",(logoutHandler)::handleRequest);
        Spark.post("/game",(createGameHandler)::handleRequest);
        Spark.get("/game",(listGameHandler)::handleRequest);
        Spark.put("/game",(joinGameHandler)::handleRequest);
        Spark.exception(Exception.class,this::errorHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    public Object errorHandler(Exception e,Request req, Response res){
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        switch (e.getMessage()) {
            case "Already Taken" -> res.status(403);
            case "Bad Request" -> res.status(400);
            case "unauthorized" -> res.status(401);
            case null, default -> res.status(500);
        }
        res.body(body);
        return body;
    }
}
