package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.ResponseException;
import model.GameData;
import responses.*;

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
    public GameData getGame(int gameID) throws Exception {
        return gameDataAccess.getGame(gameID);
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
        Collection <ListGamesResponse> responses = new ArrayList<>();
        for(GameData game : games){
            responses.add(new ListGamesResponse(game.gameID(),game.whiteUsername(),game.blackUsername(),game.gameName()));
        }
        return responses;
    }
    public void joinGame(JoinGameRequest req, String username) throws DataAccessException, ResponseException, SQLException {
        ChessGame.TeamColor playerColor;
        if(req.playerColor() == null){
            throw new DataAccessException("Bad Request");
        }
        String requestColor = req.playerColor().toLowerCase();
        if(Objects.equals(requestColor, "black")){
            playerColor = ChessGame.TeamColor.BLACK;
        }else if(Objects.equals(requestColor, "white")){
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
