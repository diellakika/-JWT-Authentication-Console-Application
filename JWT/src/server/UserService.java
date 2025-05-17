
package server;

import java.util.HashMap;
import java.util.Map;

public class UserService {
    private static final Map<String, String> users = new HashMap();

    public UserService() {
    }

    public static boolean authenticate(String username, String password) {
        return users.containsKey(username) && ((String)users.get(username)).equals(password);
    }

    static {
        users.put("Blerim Rexha", "Blera123");
        users.put("Arbena Musa", "Arbena123");
        users.put("Mergim Hoti", "Gimi123");
    }
}
