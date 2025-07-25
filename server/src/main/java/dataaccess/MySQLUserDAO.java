package dataaccess;

import com.google.gson.Gson;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLUserDAO extends MySQLdata implements UserDAO{
    public MySQLUserDAO() throws Exception{
        congifureDatabase(createStatements);
    }
    private final String[] createStatements = {
            """
                    CREATE TABLE IF NOT EXISTS user (
                    username VARCHAR(255) NOT NULL,
                    password VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL
                    )
                    """
    };

    @Override
    public UserData getUser(String username) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username,password,email FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var name = rs.getString(1);
                        var password = rs.getString("password");
                        return new UserData(name,password,rs.getString("email"));
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
            String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, userData.email());
            preparedStatement.executeUpdate();
    }
    }

    @Override
    public void clearAll() throws DataAccessException, SQLException {
        var conn = DatabaseManager.getConnection();
        try(var preparedStatment = conn.prepareStatement("DELETE FROM user")){
            preparedStatment.executeUpdate();
        }
    }
}
