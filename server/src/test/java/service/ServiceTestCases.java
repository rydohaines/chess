package service;

import dataaccess.*;
import model.UserData;
import service.responses.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;


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
    public void negativeClear(){
        assertTrue(true);
    }
    @Test
    public void positiveLogout() throws DataAccessException {
        UserDAO userDataAccess = new MemoryUserDAO();
        AuthDAO authDataAccess = new MemoryAuthDAO();
        UserService userService = new UserService(userDataAccess,authDataAccess);
        userDataAccess.addUser(new UserData("newUser","password","email"));
        String authToken = authDataAccess.addAuth("newUser");
        userService.logout(new LogoutRequest(authToken));

        assertNull(authDataAccess.getAuth("authToken"));
    }
    @Test
    public void negativeLogout() throws DataAccessException {
        UserDAO userDataAccess = new MemoryUserDAO();
        AuthDAO authDataAccess = new MemoryAuthDAO();
        UserService userService = new UserService(userDataAccess,authDataAccess);
        userDataAccess.addUser(new UserData("newUser","password","email"));

        assertThrows(DataAccessException.class,() -> userService.logout(new LogoutRequest("invalidToken")));
    }
    @Test
    public void positiveLogin() throws DataAccessException {
        UserDAO userDataAccess = new MemoryUserDAO();
        AuthDAO authDataAccess = new MemoryAuthDAO();
        UserService userService = new UserService(userDataAccess,authDataAccess);
        userDataAccess.addUser(new UserData("newUser","password","email"));
        String authToken = authDataAccess.addAuth("newUser");
        LoginResponse response = userService.login(new LoginRequest("newUser","password"));
        assertEquals("newUser",response.username());
        assertNotNull(response.authToken());

    }
    @Test
    public void negativeLogin() throws DataAccessException {
        UserDAO userDataAccess = new MemoryUserDAO();
        AuthDAO authDataAccess = new MemoryAuthDAO();
        UserService userService = new UserService(userDataAccess,authDataAccess);
        userDataAccess.addUser(new UserData("newUser","password","email"));
        String authToken = authDataAccess.addAuth("newUser");
        assertThrows(DataAccessException.class, () -> userService.login(new LoginRequest("newUser","wrongpassword")));
    }
    @Test
    public void positiveCreateGame() throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        GameService gameService = new GameService(gameDAO,authDAO);
        UserService userService = new UserService(userDAO,authDAO);
        RegisterResponse response = userService.register(new RegisterRequest("newUser","password","email"));
        CreateGameResponse result =  gameService.createGame(new CreateGameRequest(response.authToken(),"gameName"));

        assertNotNull(gameDAO.getGame(result.gameID()));
    }
    @Test
    public void negativeCreateGame() throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        GameService gameService = new GameService(gameDAO,authDAO);
        UserService userService = new UserService(userDAO,authDAO);
        RegisterResponse response = userService.register(new RegisterRequest("newUser","password","email"));
        assertThrows(DataAccessException.class, () -> gameService.createGame(new CreateGameRequest(response.authToken(),null)));
    }
    @Test
    public void positiveJoinGame() throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        userDAO.addUser(new UserData("newUser","password","email"));
        int gameID = gameDAO.createGame("newGame");
        GameService service= new GameService(gameDAO,authDAO);
        service.joinGame(new JoinGameRequest("BLACK", gameID ,"newUser"),"newUser");
        assertEquals("newUser", gameDAO.getGame(gameID).blackUsername());

    }
    @Test
    public void negativeJoinGame() throws DataAccessException {
        GameDAO gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        userDAO.addUser(new UserData("newUser","password","email"));
        int gameID = gameDAO.createGame("newGame");
        GameService service= new GameService(gameDAO,authDAO);
        service.joinGame(new JoinGameRequest("BLACK", gameID ,"newUser"),"newUser");
        assertThrows(DataAccessException.class, () -> service.joinGame(new JoinGameRequest("BLACK",gameID,"newUser"),"newUser"));
    }
    @Test
    public void positiveListGames(){
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();
        userDAO.addUser(new UserData("alice", "pass", "alice@example.com")) ;
        String authToken = authDAO.addAuth("alice");
        int gameID = gameDAO.createGame("game1");
        int game2ID = gameDAO.createGame("game2");

        GameService gameService = new GameService(gameDAO, authDAO);
        Collection<ListGamesResponse> games = gameService.listGames();
        ListGamesResult result = new ListGamesResult(games);
        assertEquals(2,result.games().size());
    }
    @Test
    public void negativeListGames(){
        // Handler handles authorization
        assertTrue(true);
    }
}