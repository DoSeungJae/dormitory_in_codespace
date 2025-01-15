package com.DormitoryBack.module.crypt;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.annotation.PostConstruct;

@Component
public class EmailEncryptor {

    @Value("${encryption.key}")
    private String encryptionKey;

    private AES256TextEncryptor textEncryptor;

    @PostConstruct
    public void init() {
        textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(encryptionKey);
    }

    public String encryptEmail(String email) {
        return textEncryptor.encrypt(email);
    }

    public String decryptEmail(String encryptedEmail) {
        return textEncryptor.decrypt(encryptedEmail);
    }

    public String hashifyEmail(String email) throws NoSuchAlgorithmException{
        MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] encodedHash=digest.digest(email.getBytes());
        return Base64.getEncoder().encodeToString(encodedHash);
    }
}