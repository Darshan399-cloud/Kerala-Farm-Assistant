package com.keralafarmers.agrinextai.utils;

import android.util.Base64;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Security utilities for encryption, hashing, and other security operations
 */
public class SecurityUtils {
    
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final int SALT_LENGTH = 16;
    
    /**
     * Hash a password with salt using SHA-256
     * @param password Plain text password
     * @return Hashed password with salt
     */
    public static String hashPassword(String password) {
        try {
            // Generate salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Create hash with salt
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt);
            byte[] hashedPassword = md.digest(password.getBytes());
            
            // Combine salt and hash
            byte[] saltedHash = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, saltedHash, 0, salt.length);
            System.arraycopy(hashedPassword, 0, saltedHash, salt.length, hashedPassword.length);
            
            // Return Base64 encoded result
            return Base64.encodeToString(saltedHash, Base64.DEFAULT);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    /**
     * Verify a password against a hashed password
     * @param password Plain text password to verify
     * @param hashedPassword Stored hashed password
     * @return True if password matches
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        try {
            // Decode the stored hash
            byte[] saltedHash = Base64.decode(hashedPassword, Base64.DEFAULT);
            
            // Extract salt
            byte[] salt = new byte[SALT_LENGTH];
            System.arraycopy(saltedHash, 0, salt, 0, SALT_LENGTH);
            
            // Extract hash
            byte[] hash = new byte[saltedHash.length - SALT_LENGTH];
            System.arraycopy(saltedHash, SALT_LENGTH, hash, 0, hash.length);
            
            // Hash the input password with extracted salt
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt);
            byte[] inputHash = md.digest(password.getBytes());
            
            // Compare hashes
            return MessageDigest.isEqual(hash, inputHash);
            
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Generate a secure random string for tokens
     * @param length Length of the random string
     * @return Random string
     */
    public static String generateSecureRandomString(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.encodeToString(bytes, Base64.URL_SAFE | Base64.NO_WRAP).substring(0, length);
    }
    
    /**
     * Validate email format
     * @param email Email string to validate
     * @return True if email format is valid
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    /**
     * Validate password strength
     * @param password Password to validate
     * @return True if password meets minimum requirements
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        // Add more complex validation if needed
        return true;
    }
    
    /**
     * Validate phone number (basic validation for Indian format)
     * @param phoneNumber Phone number to validate
     * @return True if phone number format is valid
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return false;
        }
        // Remove spaces and special characters
        String cleanNumber = phoneNumber.replaceAll("[^0-9+]", "");
        
        // Indian phone number validation (10 digits or +91 followed by 10 digits)
        return cleanNumber.matches("^[6-9]\\d{9}$") || cleanNumber.matches("^\\+91[6-9]\\d{9}$");
    }
    
    /**
     * Sanitize input string to prevent injection attacks
     * @param input Input string to sanitize
     * @return Sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Remove potentially dangerous characters
        return input.replaceAll("[<>\"'%;()&+]", "").trim();
    }
    
    /**
     * Generate a session token for user authentication
     * @return Session token
     */
    public static String generateSessionToken() {
        return generateSecureRandomString(32);
    }
    
    /**
     * Simple encryption for sensitive data storage
     * @param data Data to encrypt
     * @param key Encryption key
     * @return Encrypted data as Base64 string
     */
    public static String encrypt(String data, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(encryptedData, Base64.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }
    
    /**
     * Simple decryption for sensitive data
     * @param encryptedData Encrypted data as Base64 string
     * @param key Decryption key
     * @return Decrypted data
     */
    public static String decrypt(String encryptedData, String key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedData = cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT));
            return new String(decryptedData);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }
}
