package com.example.keepb.utils;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtils {
    
    /**
     * 加密密码
     * @param plainPassword 明文密码
     * @return 加密后的密码
     */
    public static String encrypt(String plainPassword) {
        String salt = generateSalt(); 
        String saltedPassword = plainPassword + salt;
        String hashedPassword = sha256(saltedPassword);
        return "sha256$" + salt + "$" + hashedPassword;
    }
    
    /**
     * 验证密码
     * @param plainPassword 明文密码
     * @param encryptedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean verify(String plainPassword, String encryptedPassword) {
        if (encryptedPassword == null || !encryptedPassword.startsWith("sha256$")) {
            return false;
        }
        
        String[] parts = encryptedPassword.split("\\$");
        if (parts.length != 3) {
            return false;
        }
        
        String salt = parts[1];
        String originalHash = parts[2];
        
        String saltedPassword = plainPassword + salt;
        String newHash = sha256(saltedPassword);
        
        return originalHash.equals(newHash);
    }
    
    /**
     * 生成随机盐值
     * @return 16字节的随机盐值
     */
    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.encodeToString(salt, Base64.NO_WRAP);
    }
    
    /**
     * SHA-256哈希算法实现
     * @param input 输入字符串
     * @return 哈希结果
     */
    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(hash, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256算法不可用", e);
        }
    }
} 