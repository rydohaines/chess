package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import org.eclipse.jetty.server.Authentication;
import service.LoginRequest;
import service.LoginResponse;
import service.LogoutRequest;
import service.UserService;
import spark.Request;
import spark.Response;

public class LogoutHandler implements Handler{
    private final UserService service;
    public LogoutHandler(UserService service){
        this.service = service;
    }
    @Override
    public Object handleRequest(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        LogoutRequest request = new LogoutRequest(authToken);
        service.logout(request);
        res.status(200);
        res.type("application/json");
        return "";
    }
}
