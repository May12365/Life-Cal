package life.cal;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {
    private static final SecureRandom RAND = new SecureRandom();
    private static final int ITER = 10000;
    private static final int KEYLEN = 256;

    public static String newSalt() {
        byte[] salt = new byte[16];
        RAND.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    public static String hash(String password, String b64Salt) {
        try {
            byte[] salt = Base64.getDecoder().decode(b64Salt);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITER, KEYLEN);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) { throw new RuntimeException(e); }
    }
    public static boolean verify(String raw, String b64Salt, String storedHash) {
        return hash(raw, b64Salt).equals(storedHash);
    }
}
