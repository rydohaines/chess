package server.handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.*;
import service.ResponsesRequests.LoginRequest;
import service.ResponsesRequests.LoginResponse;
import spark.Request;
import spark.Response;

public class LoginHandler implements Handler {
    private final UserService service;
    public LoginHandler(UserService service){
        this.service = service;
    }
    @Override
    public Object handleRequest(Request req, Response res) throws DataAccessException {
        var gson = new Gson();
        String body = req.body();
        LoginRequest request  = (LoginRequest)gson.fromJson(body, LoginRequest.class);
        LoginResponse result = service.login(request);
        return gson.toJson(result);
    }
}
