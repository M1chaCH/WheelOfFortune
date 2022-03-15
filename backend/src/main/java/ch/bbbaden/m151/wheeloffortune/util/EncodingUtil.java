package ch.bbbaden.m151.wheeloffortune.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncodingUtil {

    private EncodingUtil() {}

    public static String hashString(String toHash, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(toHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes)
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static String generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return new String(salt, StandardCharsets.UTF_8);
    }
}
