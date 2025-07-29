package dataaccess;

import model.UserData;

import java.sql.SQLException;
import java.util.UUID;

public class MySQLAuthDAO extends MySQLdata implements AuthDAO{

    public MySQLAuthDAO() throws ResponseException, DataAccessException {
        congifureDatabase(createStatements);
    }
    @Override
    public String addAuth(String username) throws DataAccessException, SQLException {
        String authToken = UUID.randomUUID().toString();
        var conn = DatabaseManager.getConnection();
        try(var preparedStatment = conn.prepareStatement("INSERT INTO auth (username, authToken) VALUES (?,?)")){
            preparedStatment.setString(1,username);
            preparedStatment.setString(2,authToken);
            preparedStatment.executeUpdate();
        }
        return authToken;
    }
    private final String[] createStatements = {
            """
                    CREATE TABLE IF NOT EXISTS auth (
                    username VARCHAR(255) NOT NULL,
                    authToken VARCHAR(255) NOT NULL
                    )
                    """
    };

    @Override
    public void clearAll() throws DataAccessException, SQLException {
        var conn = DatabaseManager.getConnection();
        try(var preparedStatment = conn.prepareStatement("DELETE FROM auth")){
            preparedStatment.executeUpdate();
        }
    }

    @Override
    public String getAuth(String authToken) throws DataAccessException, SQLException {
        var conn = DatabaseManager.getConnection();
        var statement = "SELECT authToken FROM auth WHERE authToken=?";
        try(var ps = conn.prepareStatement(statement)) {
            ps.setString(1, authToken);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return authToken;
                }
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
            return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
    var conn = DatabaseManager.getConnection();
    try(var ps = conn.prepareStatement("DELETE FROM auth WHERE authToken=?")) {
        ps.setString(1,authToken);
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }
    }

    @Override
    public String getUser(String authToken) throws DataAccessException, SQLException {
        var conn = DatabaseManager.getConnection();
        try(var ps = conn.prepareStatement("SELECT username FROM auth WHERE authToken=?")) {
            ps.setString(1,authToken);
            try(var rs = ps.executeQuery()) {
                if (rs.next()) {
                   return rs.getString(1);
                }
            }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
        return null;
    }
}
