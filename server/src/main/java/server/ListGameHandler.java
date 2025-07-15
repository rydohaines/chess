package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.GameService;
import service.ListGamesResponse;
import spark.Request;
import spark.Response;

public class ListGameHandler implements Handler{
    private final GameService service;
    public ListGameHandler(GameService service){
        this.service = service;
    }

    @Override
    public Object handleRequest(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        authorizeToken(authToken,service.getAuthDataAccess());
        var gson = new Gson();
        ListGamesResponse result = service.listGames();
        return gson.toJson(result);
    }
}
