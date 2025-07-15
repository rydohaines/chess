package dataaccess;

import model.GameData;


import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO{
    private final Collection<GameData> GameDatabase = new ArrayList<>();

    @Override
    public void clearAll() {
        GameDatabase.clear();
    }
}
