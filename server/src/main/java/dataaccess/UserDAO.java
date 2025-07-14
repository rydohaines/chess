package dataaccess;
import model.UserData;
public interface UserDAO {
    public UserData getUser(String username);
    public void addUser(UserData userData);
}
