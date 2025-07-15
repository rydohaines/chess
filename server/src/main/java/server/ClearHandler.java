package server;

import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler implements Handler {
    private final ClearService service  = new ClearService();
    public Object handleRequest(Request req, Response res){
        service.clear();
        return res;
    }

}
