package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO{
    public MySQLUserDAO() throws Exception{
        congifureDatabase();
    }
    private final String[] createStatements = {
            """
                    CREATE TABLE IF NOT EXISTS user (
                    username VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL)
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
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void addUser(UserData userData) {

    }

    @Override
    public void clearAll() {

    }
}
