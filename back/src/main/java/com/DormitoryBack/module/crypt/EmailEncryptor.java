package com.DormitoryBack.module.crypt;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
}