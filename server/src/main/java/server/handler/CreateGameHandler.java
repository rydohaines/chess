package server.handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.*;
import responses.*;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class CreateGameHandler implements Handler {
    private GameService service;
    public CreateGameHandler(GameService service){
        this.service = service;
    }
    @Override
    public Object handleRequest(Request req, Response res) throws DataAccessException, SQLException {
        String authToken = req.headers("authorization");
        authorizeToken(authToken, service.getAuthDataAccess());
        var gson = new Gson();
        String body = req.body();
        CreateGameRequest request = (CreateGameRequest)gson.fromJson(body,CreateGameRequest.class);
        CreateGameResponse result = service.createGame(request);
        return gson.toJson(result);
    }
}
