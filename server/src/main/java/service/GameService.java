package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.ResponseException;
import model.GameData;
import service.responses.CreateGameRequest;
import service.responses.CreateGameResponse;
import service.responses.JoinGameRequest;
import service.responses.ListGamesResponse;

import java.sql.SQLException;
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
    public CreateGameResponse createGame(CreateGameRequest req) throws DataAccessException, SQLException {
        if(req.gameName()== null){
            throw new DataAccessException("Bad Request");
        }
        return new CreateGameResponse(gameDataAccess.createGame(req.gameName()));
    }
    public AuthDAO getAuthDataAccess(){
        return authDataAccess;
    }
    public Collection<ListGamesResponse> listGames() throws ResponseException {
        Collection <GameData> games = gameDataAccess.listGames();
        Collection < ListGamesResponse> responses = new ArrayList<>();
        for(GameData game : games){
            responses.add(new ListGamesResponse(game.gameID(),game.whiteUsername(),game.blackUsername(),game.gameName()));
        }
        return responses;
    }
    public void joinGame(JoinGameRequest req, String username) throws DataAccessException, ResponseException {
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
