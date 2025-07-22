package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO {
     String addAuth(String username) throws DataAccessException, SQLException;
     void clearAll() throws DataAccessException, SQLException;
     String getAuth(String authToken) throws DataAccessException, SQLException;
     void deleteAuth(String authToken);
     String getUser(String authToken);
}
