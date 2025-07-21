import chess.*;
import dataaccess.*;
import server.Server;
import service.ClearService;
import service.GameService;
import service.UserService;

public class Main {
    public static void main(String[] args) throws Exception {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        UserDAO userDataAccess = new MySQLUserDAO();
        AuthDAO authDataAccess = new MySQLAuthDAO();
        GameDAO gameDataAccess = new MySQLGameDAO();
        UserService userService = new UserService(userDataAccess,authDataAccess);
        GameService gameService = new GameService(gameDataAccess,authDataAccess);
        ClearService clearService = new ClearService(userDataAccess,authDataAccess,gameDataAccess);
        Server server = new Server(userService,gameService,clearService);
        server.run(8080);
    }
}