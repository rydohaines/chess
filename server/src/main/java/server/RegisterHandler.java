package server;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Authentication;
import service.RegisterRequest;
import service.RegisterResponse;
import service.UserService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
private final UserService service = new UserService();
public Object handleRequest(Request req, Response res) {
        var gson = new Gson();
        String body = req.body();
RegisterRequest request = (RegisterRequest)gson.fromJson(body,RegisterRequest.class);
RegisterResponse result = service.register(request);
return gson.toJson(result);
    }
}
