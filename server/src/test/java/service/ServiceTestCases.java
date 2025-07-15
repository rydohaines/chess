package service;

import dataaccess.*;
import model.UserData;
import service.ResponsesRequests.RegisterRequest;
import service.ResponsesRequests.RegisterResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Objects;



public class ServiceTestCases {
    @Test
    public void positiveRegister() throws DataAccessException {
        UserDAO userDataAccess = new MemoryUserDAO();
        AuthDAO authDataAccess = new MemoryAuthDAO();
        UserService userService = new UserService(userDataAccess,authDataAccess);
        RegisterRequest request = new RegisterRequest("NewUsername","newpassword","newuser@test.com");
        RegisterResponse result = userService.register(request);
        assertEquals("NewUsername", result.username());
        assertNotNull(result.authToken());
    }
    @Test
    public void negativeRegister() throws DataAccessException{
        UserDAO userDataAccess = new MemoryUserDAO();
        AuthDAO authDataAccess = new MemoryAuthDAO();
        UserService userService = new UserService(userDataAccess,authDataAccess);
        RegisterRequest request = new RegisterRequest("NewUsername","newpassword","newuser@test.com");
        RegisterResponse result = userService.register(request);
        RegisterRequest repeatRequest = new RegisterRequest("NewUsername","newpassword","newuser@test.com");
        assertThrows(DataAccessException.class, () -> userService.register(repeatRequest));
    }
    @Test
    public void positiveClear() throws DataAccessException {
        UserDAO userDataAccess = new MemoryUserDAO();
        AuthDAO authDataAccess = new MemoryAuthDAO();
        GameDAO gameDataAccess = new MemoryGameDAO();
        ClearService clearService = new ClearService(userDataAccess,authDataAccess,gameDataAccess);
        userDataAccess.addUser(new UserData("newUser","password","email"));
        authDataAccess.addAuth("newUser");
        gameDataAccess.createGame("testGame");
        clearService.clear();

        assertNull(userDataAccess.getUser("newUser"));
        assertNull(authDataAccess.getAuth("newUser"));
        assertTrue(gameDataAccess.listGames().isEmpty());
    }
    @Test
    public void positiveLogout(){

    }
    public void negativeLogout(){

    }
    public void positiveLogin(){

    }
    public void negativeLogin(){

    }
    public void positiveCreateGame(){

    }
    public void negativeCreateGame(){

    }
    public void positiveJoinGame(){

    }
    public void negativeJoinGame(){

    }
    public void positiveListGames(){

    }
    public void negativeListGames(){

    }
}
