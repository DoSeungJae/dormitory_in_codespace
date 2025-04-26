package com.DormitoryBack.module.crypt;

import org.jasypt.util.text.AES256TextEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.annotation.PostConstruct;

@Component
public class PIEncryptor {

    @Value("${encryption.key}")
    private String encryptionKey;

    private AES256TextEncryptor textEncryptor;

    @PostConstruct
    public void init() {
        textEncryptor = new AES256TextEncryptor();
        textEncryptor.setPassword(encryptionKey);
    }

    public String encrypt_AES256(String pi) { //encryptEmail
        return textEncryptor.encrypt(pi);
    }

    public String decrypt_AES256(String encryptedPI) { //decryptEmail
        return textEncryptor.decrypt(encryptedPI);
    }

    public String hashify(String pi) throws NoSuchAlgorithmException{ //hashifyEmail
        MessageDigest digest=MessageDigest.getInstance("SHA-256");
        byte[] encodedHash=digest.digest(pi.getBytes());
        
        //byte[] encodedHash=digest.digest((pi+encryptionKey).getBytes());
        //보안성 강화를 위해 이 코드로 변경 필요. 테스트 해본적 없음.
        return Base64.getEncoder().encodeToString(encodedHash);
    }
}