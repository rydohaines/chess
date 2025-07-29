package repls;

import server.ServerFacade;
import service.responses.CreateGameRequest;

import java.util.Arrays;

public class PostLoginClient implements Client {
    private final ServerFacade serverFacade;
    private final String serverUrl;
    private final Repl repl;

    public PostLoginClient(String serverUrl, Repl repl) {
        this.serverUrl = serverUrl;
        serverFacade = new ServerFacade(serverUrl);
        this.repl = repl;
    }

    @Override
    public String help() {
        return """
                create <NAME> - a game
                list - games
                join <ID> [WHITE|BLACK] - a game
                observe <ID> - a game
                logout - when you are done
                quit - playing chess
                help - with possible commands
                """;
    }

    @Override
    public String eval(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        } catch (Exception ex) {
            return ex.getMessage();
        }
        return null;
    }
}
