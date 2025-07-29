package repls;

import server.ServerFacade;
import service.responses.LoginRequest;
import service.responses.LoginResponse;
import service.responses.RegisterRequest;
import service.responses.RegisterResponse;

import java.util.Arrays;

public class LoginClient {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private final Repl repl;
    public LoginClient(String serverUrl, Repl repl) {
        this.serverUrl = serverUrl;
        serverFacade = new ServerFacade(serverUrl);
        this.repl = repl;
    }
    public String help(){
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account"
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                """;
    }
    public String eval(String line){
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
    public String login(String ... params) throws Exception {
        LoginRequest request = new LoginRequest(params[0],params[1]);
        LoginResponse response = serverFacade.login(request);
        return "You logged in as " + response.username();
    }
    public String register(String ... params) throws Exception {
        RegisterRequest request = new RegisterRequest(params[0],params[1],params[2]);
        RegisterResponse response = serverFacade.register(request);
        return "You logged in as " +response.username();
    }

}
