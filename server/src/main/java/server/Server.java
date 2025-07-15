package server;


import com.google.gson.Gson;
import org.eclipse.jetty.server.Authentication;
import service.UserService;
import spark.*;

import java.util.Map;
import java.util.Objects;

public class Server {
    private UserService UserService;
    private final RegisterHandler registerHandler = new RegisterHandler(UserService);
private final ClearHandler clearHandler = new ClearHandler();
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
        else {
            res.status(500);
        }
        res.body(body);
        return body;
    }
}
