package server.handler;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.ResponseException;
import spark.Request;
import spark.Response;

public interface Handler {
    public Object handleRequest(Request req, Response res) throws Exception;
    default String authorizeToken(String authToken, AuthDAO authDataAccess) throws DataAccessException{
        if(authDataAccess.getAuth(authToken) == null){
            throw new DataAccessException("unauthorized");
        }
        return authDataAccess.getUser(authToken);
    }
}
