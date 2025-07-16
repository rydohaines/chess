package dataaccess;

import chess.ChessGame;
import model.GameData;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO{
    private final Collection<GameData> GameDatabase = new ArrayList<>();

    @Override
    public void clearAll() {
        GameDatabase.clear();
    }
    public int createGame(String gameName){
        int gameID = GameDatabase.size()+100;
        GameData game = new GameData(gameID,null,null,gameName,new ChessGame());
        GameDatabase.add(game);
        return gameID;
    }
    public Collection<GameData> listGames(){
    return GameDatabase;
    }
    public GameData getGame(int gameID){
        for(GameData data : GameDatabase){
            if(data.gameID() == gameID){
                return data;
            }
        }
        return null;
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
        GameDatabase.remove(game);
        GameDatabase.add(newGame);
    }
}
