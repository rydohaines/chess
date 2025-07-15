package dataaccess;

import model.AuthData;

public interface AuthDAO {
     String addAuth(String username);
     void clearAll();
     String getAuth(String authToken);
     void deleteAuth(String authToken);
     String getUser(String authToken);
}
