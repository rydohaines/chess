package server.handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import service.GameService;
import responses.ListGamesResponse;
import responses.ListGamesResult;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.Collection;

public class ListGameHandler implements Handler {
    private final GameService service;
    public ListGameHandler(GameService service){
        this.service = service;
    }

    @Override
    public Object handleRequest(Request req, Response res) throws DataAccessException, SQLException, ResponseException {
        String authToken = req.headers("authorization");
        authorizeToken(authToken,service.getAuthDataAccess());
        var gson = new Gson();
        Collection<ListGamesResponse> result = service.listGames();
        return gson.toJson(new ListGamesResult(result));
    }
}
