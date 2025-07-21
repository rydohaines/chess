package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class MySQLGameDAO extends MySQLdata implements GameDAO{

    @Override
    public void clearAll() {

    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void updateGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException {

    }
}
