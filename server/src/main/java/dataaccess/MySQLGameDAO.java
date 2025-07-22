package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

public class MySQLGameDAO extends MySQLdata implements GameDAO{

    public MySQLGameDAO() throws ResponseException, DataAccessException {
        congifureDatabase(createStatements);
    }
    private final String[] createStatements = {
            """ 
            CREATE TABLE IF NOT EXISTS game (
            gameID INT NOT NULL AUTO_INCREMENT, whiteUsername VARCHAR(255),blackUsername VARCHAR(255),
            gameName VARCHAR(255), jsonGame TEXT NOT NULL,
            PRIMARY KEY( gameID ))
"""
    };
    @Override
    public void clearAll() throws SQLException, DataAccessException {
        var conn = DatabaseManager.getConnection();
        try(var ps = conn.prepareStatement("DELETE FROM game")){
            ps.executeUpdate();
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException, SQLException {
        var conn = DatabaseManager.getConnection();
        ChessGame newGame =  new ChessGame();
        var json = new Gson().toJson(newGame);
        try(var preparedStatement = conn.prepareStatement("INSERT INTO game (gameName, jsonGame) VALUES (?,?)",Statement.RETURN_GENERATED_KEYS)){
            preparedStatement.setString(1,gameName);
            preparedStatement.setString(2,json);
            preparedStatement.executeUpdate();
            var rs = preparedStatement.getGeneratedKeys();
            if(rs.next()){
                return rs.getInt(1);
            }
        }
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
