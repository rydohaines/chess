package client;

import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import server.handler.ClearHandler;
import service.responses.LoginRequest;
import service.responses.RegisterRequest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() throws Exception {
        facade.clearAll();
        server.stop();
    }

    @BeforeEach
    void clearAll() throws Exception {
        facade.clearAll();
    }

    @Test
    public void positiveRegisterTest() throws Exception {
        var authData = facade.register(new RegisterRequest("username","password","email"));
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void negativeRegisterTest() throws Exception{
        facade.register(new RegisterRequest("username","password","email"));
        assertThrows(Exception.class,  () -> facade.register(new RegisterRequest("username","password","email")));
    }

    @Test
    public void positiveLoginTest() throws Exception{
        facade.register(new RegisterRequest("username","password","email"));
        var authData = facade.login(new LoginRequest("username","password"));
        assertTrue(authData.authToken().length() > 10);
    }
    @Test
    public void negativeLoginTest() throws Exception{
        facade.register(new RegisterRequest("username","password","email"));
        assertThrows(Exception.class, () -> facade.login(new LoginRequest("username","WRONGpassword")));
    }

}
