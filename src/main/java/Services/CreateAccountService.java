package Services;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountService {

    private static final Map<String, String> userDatabase = new HashMap<>(); // Simulating a database

    public boolean isValidUser(String username, String password) {
        return userDatabase.containsKey(username) && userDatabase.get(username).equals(password);
    }

    public boolean createMemberAccount(String username, String password) {
        if (userDatabase.containsKey(username)) {
            return false; // Username already exists
        }
        userDatabase.put(username, password); // Store new user
        return true;
    }
}
