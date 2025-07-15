package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;

public class GameService {
    private final GameDAO gameDataAccess;
    private final AuthDAO authDataAccess;
    public GameService(GameDAO gameDataAccess, AuthDAO authDataAccess){
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }
    public CreateGameResponse createGame(CreateGameRequest req) throws DataAccessException{
        if(req.gameName()== null){
            throw new DataAccessException("Bad Request");
        }
        return new CreateGameResponse(gameDataAccess.createGame(req.gameName()));
    }
    public AuthDAO getAuthDataAccess(){
        return authDataAccess;
    }
}
