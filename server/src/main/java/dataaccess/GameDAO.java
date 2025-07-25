package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public interface GameDAO{
void clearAll() throws SQLException, DataAccessException;
int createGame(String gameName) throws DataAccessException, SQLException;
Collection<GameData> listGames() throws ResponseException;
GameData getGame(int gameID) throws ResponseException;
void updateGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException, SQLException;
}
