package server;


import com.google.gson.Gson;
import dataaccess.*;
import org.eclipse.jetty.server.Authentication;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Map;
import java.util.Objects;

public class Server {
    private final UserDAO userDatabase = new MemoryUserDAO();
    private final AuthDAO authDatabase = new MemoryAuthDAO();
    private final GameDAO gameDatabase = new MemoryGameDAO();
    private final UserService userService = new UserService(userDatabase,authDatabase);
    private final GameService gameService = new GameService(gameDatabase,authDatabase);
    private final ClearService clearService = new ClearService(userDatabase,authDatabase,gameDatabase);
    private final Handler registerHandler = new RegisterHandler(userService);
    private final Handler clearHandler = new ClearHandler(clearService);


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (registerHandler)::handleRequest);
        Spark.delete("/db",(clearHandler)::handleRequest);
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
        if(Objects.equals(e.getMessage(), "Already Taken")){
            res.status(403);
        }
        else if(Objects.equals(e.getMessage(),"Bad Request")){
            res.status(400);
        }
        else {
            res.status(500);
        }
        res.body(body);
        return body;
    }
}
