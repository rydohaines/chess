package service;

import dataaccess.*;
import model.UserData;

public class UserService {
    private final UserDAO dataAccess;
    public UserService(UserDAO dataAccess){
        this.dataAccess = dataAccess;
    }
    private final AuthDAO authDataAccess = new MemoryAuthDAO();
    public RegisterResponse register(RegisterRequest req ) throws DataAccessException {
        UserData user = new UserData(req.username(), req.password(), req.email());
        if(dataAccess.getUser(req.username()) == null){
            dataAccess.addUser(user);
            return new RegisterResponse(req.username(),authDataAccess.addAuth(req.username()));
        }
        else throw new DataAccessException("Already Taken");
    }
}
