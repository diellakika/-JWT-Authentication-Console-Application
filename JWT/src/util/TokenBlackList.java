import java.util.HashSet;
import java.util.Set;

public class TokenBlacklist {
    private static final Set<String> blacklistedTokens = new HashSet<>();

    public static void add(String token) {
        blacklistedTokens.add(token);
    }

    public static boolean contains(String token) {
        return blacklistedTokens.contains(token);
    }
}

