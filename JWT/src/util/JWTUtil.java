package util;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

public class JwtUtil {

private static final String SECRET = "shume-sekret"; // për HS256

private static RSAPrivateKey loadPrivateKey() throws Exception {
String key = new String(Files.readAllBytes(Paths.get("src/main/resources/private.pem")));
key = key.replace("-----BEGIN PRIVATE KEY-----", "")
.replace("-----END PRIVATE KEY-----", "")
.replaceAll("\\s+", "");

byte[] keyBytes = Base64.getDecoder().decode(key);
PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
KeyFactory kf = KeyFactory.getInstance("RSA");
return (RSAPrivateKey) kf.generatePrivate(spec);
}

public enum AlgorithmType {
RS256, HS256
}

public static String generateToken(String username, AlgorithmType type) {
try {
Algorithm algorithm;

if (type == AlgorithmType.RS256) {
RSAPrivateKey privateKey = loadPrivateKey();
algorithm = Algorithm.RSA256(null, privateKey);
} else if (type == AlgorithmType.HS256) {
algorithm = Algorithm.HMAC256(SECRET);
} else {
throw new IllegalArgumentException("Unsupported algorithm type");
}

return JWT.create()
.withSubject(username)
.withIssuedAt(new Date())
.withExpiresAt(new Date(System.currentTimeMillis() + 3600_000)) // 1h expiry
.sign(algorithm);

} catch (IOException e) {
throw new RuntimeException("Key loading failed: " + e.getMessage(), e);
} catch (Exception e) {
throw new RuntimeException("Error generating JWT: " + e.getMessage(), e);
}
}
}
