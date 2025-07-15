package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearService {
    private final GameDAO gameDataAccess;
    private final AuthDAO authDataAccess;
    private final UserDAO userDataAccess;
    public ClearService(UserDAO userDataAccess, AuthDAO authDataAccess,GameDAO gameDataAccess){
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
        this.userDataAccess = userDataAccess;
    }
    public void clear(){
    gameDataAccess.clearAll();
    authDataAccess.clearAll();
    userDataAccess.clearAll();
}
}
