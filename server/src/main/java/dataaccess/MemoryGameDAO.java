package dataaccess;

import chess.ChessGame;
import model.GameData;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO{
    private final Collection<GameData> gameDatabase = new ArrayList<>();

    @Override
    public void clearAll() {
        gameDatabase.clear();
    }
    public int createGame(String gameName){
        int gameID = gameDatabase.size()+100;
        GameData game = new GameData(gameID,null,null,gameName,new ChessGame());
        gameDatabase.add(game);
        return gameID;
    }
    public Collection<GameData> listGames(){
    return gameDatabase;
    }
    public GameData getGame(int gameID){
        for(GameData data : gameDatabase){
            if(data.gameID() == gameID){
                return data;
            }
        }
        return null;
    }

    @Override
    public void updateBoard(int gameID, ChessGame game) throws DataAccessException, Exception {

    }

    @Override
    public void removeUser(int gameID, String username, ChessGame.TeamColor playerColor) throws ResponseException, DataAccessException, SQLException {

    }

    public void updateGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException {
        GameData game = getGame(gameID);
        GameData newGame = null;
        if(playerColor == ChessGame.TeamColor.WHITE ){
            if(game.whiteUsername() != null){
                throw new DataAccessException("Already Taken");
            }else {
                newGame = new GameData(gameID,username, game.blackUsername(),game.gameName(),game.game());
            }
        }
        if(playerColor == ChessGame.TeamColor.BLACK){
            if(game.blackUsername() != null){
                throw new DataAccessException("Already Taken");
            }
            else {
                newGame = new GameData(gameID,game.whiteUsername(),username,game.gameName(),game.game());
            }
        }
        gameDatabase.remove(game);
        gameDatabase.add(newGame);
    }
}
