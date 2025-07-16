package server.handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.GameService;
import service.responses.JoinGameRequest;
import spark.Request;
import spark.Response;

public class JoinGameHandler implements Handler {
    private final GameService service;
    public JoinGameHandler(GameService service){
        this.service = service;
    }

    @Override
    public Object handleRequest(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        String username = authorizeToken(authToken,service.getAuthDataAccess());
        var gson = new Gson();
        String body = req.body();
        JoinGameRequest request = gson.fromJson(body, JoinGameRequest.class);
        service.joinGame(request,username);
        res.status(200);
        res.type("application/json");
        return "";

    }
}
