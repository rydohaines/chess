package server;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import spark.Request;
import spark.Response;

public interface Handler {
    public Object handleRequest(Request req, Response res) throws DataAccessException;
    default void authorizeToken(String authToken, AuthDAO authDataAccess) throws DataAccessException{
        if(authDataAccess.getAuth(authToken) == null){
            throw new DataAccessException("unauthorized");
        }
    }
}
