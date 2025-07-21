package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO{
    public MySQLUserDAO() throws Exception{
        congifureDatabase();
    }
    private final String[] createStatements = {
            """
                    CREATE TABLE IF NOT EXISTS user (
                    username VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    PRIMARY KEY (username) )
                    """
    };
    private void congifureDatabase() throws DataAccessException, ResponseException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

    @Override
    public UserData getUser(String username) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ResultSet resultSet = ps.getGeneratedKeys();
                        return new UserData(resultSet.getString(1),null,null);
                    }
                }
            }
        } catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void addUser(UserData userData) throws DataAccessException, SQLException {
        var conn  = DatabaseManager.getConnection();
        try(var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES (?, ?, ?)")){
            preparedStatement.setString(1, userData.username());
            preparedStatement.setString(2, userData.password());
            preparedStatement.setString(3, userData.email());
            preparedStatement.executeUpdate();
    }
    }

    @Override
    public void clearAll() {

    }
}
