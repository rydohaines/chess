package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO {
     String addAuth(String username) throws DataAccessException, SQLException;
     void clearAll();
     String getAuth(String authToken);
     void deleteAuth(String authToken);
     String getUser(String authToken);
}
