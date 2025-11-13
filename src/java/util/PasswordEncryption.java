package util;

import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.catalina.realm.SecretKeyCredentialHandler;

public class PasswordEncryption {
    private static SecretKeyCredentialHandler handler;
    
    static {
        try {
            handler = new SecretKeyCredentialHandler();
            handler.setAlgorithm("PBKDF2WithHmacSHA512");
            handler.setIterations(10000);
            handler.setKeyLength(32);
            handler.setSaltLength(16);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordEncryption.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Failed to initialize password handler", ex);
        }
    }
    
    public static String hashPassword(String plainPassword) {
        return handler.mutate(plainPassword);
    }
    
    public static boolean checkPassword(String plainPassword, String storedHash) {
        return handler.matches(plainPassword, storedHash);
    }
}
