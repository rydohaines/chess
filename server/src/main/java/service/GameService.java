package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import service.ResponsesRequests.CreateGameRequest;
import service.ResponsesRequests.CreateGameResponse;
import service.ResponsesRequests.JoinGameRequest;
import service.ResponsesRequests.ListGamesResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

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
    public Collection<ListGamesResponse> listGames(){
        Collection <GameData> games = gameDataAccess.listGames();
        Collection < ListGamesResponse> responses = new ArrayList<>();
        for(GameData game : games){
            responses.add(new ListGamesResponse(game.gameID(),game.whiteUsername(),game.blackUsername(),game.gameName()));
        }
        return responses;
    }
    public void joinGame(JoinGameRequest req, String username) throws DataAccessException {
        ChessGame.TeamColor playerColor;
        if(Objects.equals(req.playerColor(), "BLACK")){
            playerColor = ChessGame.TeamColor.BLACK;
        }else if(Objects.equals(req.playerColor(), "WHITE")){
            playerColor = ChessGame.TeamColor.WHITE;
        }
        else {
            throw new DataAccessException("Bad Request");
        }
        if(gameDataAccess.getGame(req.gameID()) == null){
            throw new DataAccessException("Bad Request");
        }
        gameDataAccess.updateGame(req.gameID(),playerColor,username);
    }
}
