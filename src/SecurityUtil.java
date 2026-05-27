import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public final class SecurityUtil {
    private static final SecureRandom RNG = new SecureRandom();

    private SecurityUtil() {}

    public static String newSaltHex(int bytes) {
        byte[] b = new byte[bytes];
        RNG.nextBytes(b);
        return toHex(b);
    }

    public static String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] out = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return toHex(out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte x : data) sb.append(String.format("%02x", x));
        return sb.toString();
    }
}

