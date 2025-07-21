package server.handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import service.*;
import service.responses.LoginRequest;
import service.responses.LoginResponse;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class LoginHandler implements Handler {
    private final UserService service;
    public LoginHandler(UserService service){
        this.service = service;
    }
    @Override
    public Object handleRequest(Request req, Response res) throws DataAccessException, ResponseException, SQLException {
        var gson = new Gson();
        String body = req.body();
        LoginRequest request  = (LoginRequest)gson.fromJson(body, LoginRequest.class);
        LoginResponse result = service.login(request);
        return gson.toJson(result);
    }
}
