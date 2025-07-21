package dataaccess;
import model.UserData;

import java.sql.SQLException;

public interface UserDAO {
    public UserData getUser(String username) throws ResponseException;
    public void addUser(UserData userData) throws DataAccessException, SQLException;
    void clearAll();
}
