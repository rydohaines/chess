package server.handler;

import dataaccess.DataAccessException;
import service.ClearService;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class ClearHandler implements Handler {
    private final ClearService service;
    public ClearHandler(ClearService service){
        this.service = service;
    }
    public Object handleRequest(Request req, Response res) throws SQLException, DataAccessException {
        service.clear();
        res.status(200);
        res.type("application/json");
        return "";
    }

}
