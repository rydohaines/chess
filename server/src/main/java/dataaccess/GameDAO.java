package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public interface GameDAO{
void clearAll();
int createGame(String gameName);
}
