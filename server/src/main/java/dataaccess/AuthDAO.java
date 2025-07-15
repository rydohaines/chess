package dataaccess;

import model.AuthData;

public interface AuthDAO {
     String addAuth(String username);
     void clearAll();
}
