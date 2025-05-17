package util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.interfaces.RSAPrivateKey;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;


public class JwtUtil {

    private static RSAPrivateKey loadPrivateKey() throws Exception {
        // Load the private key from file
        String key = new String(Files.readAllBytes(Paths.get("src/main/resources/private.pem")));
        key = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    public static String generateToken(String username) {
        try {
            RSAPrivateKey privateKey = loadPrivateKey();
            Algorithm algorithm = Algorithm.RSA256(null, privateKey); // Only private key needed for signing

          

        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT: " + e.getMessage(), e);
        }
    }
}
