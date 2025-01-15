package com.DormitoryBack.domain.member.domain.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DormitoryBack.domain.member.domain.entity.EncryptedEmailMap;
import com.DormitoryBack.domain.member.domain.repository.EncryptedEmailRepository;
import com.DormitoryBack.module.crypt.EmailEncryptor;

@Service
public class EncryptedEmailService {
    
    @Autowired
    private EmailEncryptor emailEncryptor;

    @Autowired
    private EncryptedEmailRepository encryptedEmailRepository;

    public String getOriginEmail(String hashEmail){
        EncryptedEmailMap emailMap=encryptedEmailRepository.findByEmailHash(hashEmail);
        if(emailMap==null){
            return null;
        }
        return emailEncryptor.decryptEmail(emailMap.getEmailAES256());
    }

    public void setNewEmailMap(String originEmail){
        String hashEmail;
        try{
            hashEmail=emailEncryptor.hashifyEmail(originEmail);
        }catch(NoSuchAlgorithmException e){
            return ;
        }
        
        EncryptedEmailMap emailMap=EncryptedEmailMap.builder()
            .emailAES256(emailEncryptor.encryptEmail(originEmail))
            .emailHash(hashEmail)
            .build();
        
        encryptedEmailRepository.save(emailMap);
    }
    
}
