package dataaccess;

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
    public void clearAll() {

    }

    @Override
    public String getAuth(String authToken) {
        return "";
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public String getUser(String authToken) {
        return "";
    }
}
