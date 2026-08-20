package gu.common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 비밀번호 salt 생성 및 salt+SHA-256 해시.
 * 기존 DB의 SHA2(x,256)(hex 64자)와 동일한 인코딩 규칙을 유지해서
 * salt 없는 레거시 해시와도 비교할 수 있게 한다.
 */
public class PasswordUtil {
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String newSalt() {
        byte[] bytes = new byte[16];
        RANDOM.nextBytes(bytes);
        return toHex(bytes);
    }

    public static String hash(String rawPw, String salt) {
        return sha256Hex(salt + rawPw);
    }

    public static String hashLegacy(String rawPw) {
        return sha256Hex(rawPw);
    }

    public static boolean matches(String rawPw, String salt, String storedHash) {
        if (storedHash == null) {
            return false;
        }
        return MessageDigest.isEqual(hash(rawPw, salt).getBytes(StandardCharsets.UTF_8), storedHash.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean matchesLegacy(String rawPw, String storedHash) {
        if (storedHash == null) {
            return false;
        }
        return MessageDigest.isEqual(hashLegacy(rawPw).getBytes(StandardCharsets.UTF_8), storedHash.getBytes(StandardCharsets.UTF_8));
    }

    private static String sha256Hex(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return toHex(md.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("SHA-256 알고리즘을 사용할 수 없습니다", ex);
        }
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
