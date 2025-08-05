package repls;

import model.GameData;

public interface Client {
    String help();

    String eval(String line);

    void updateBoard(GameData game);
}
