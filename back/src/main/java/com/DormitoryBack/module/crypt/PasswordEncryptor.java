package com.DormitoryBack.module.crypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncryptor {
    
    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordEncryptor(){
        this.passwordEncoder=new BCryptPasswordEncoder();
    }

    public String encryptPassword(String password){
        return passwordEncoder.encode(password);
    }

    public String hashifyEmail(String email){
        return passwordEncoder.encode(email);
    }

    public boolean matchesPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
