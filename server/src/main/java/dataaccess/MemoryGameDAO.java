package dataaccess;

import chess.ChessGame;
import model.GameData;


import java.util.ArrayList;
import java.util.Collection;

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

    }
}
