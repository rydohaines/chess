package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public interface GameDAO{
void clearAll();
int createGame(String gameName);
Collection<GameData> listGames();
GameData getGame(int gameID);
void updateGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException;
}
