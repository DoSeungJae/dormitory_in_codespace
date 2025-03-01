package com.DormitoryBack.domain.member.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DormitoryBack.domain.member.domain.entity.EncryptedEmailMap;
import com.DormitoryBack.domain.member.domain.repository.EncryptedEmailRepository;
import com.DormitoryBack.module.crypt.PIEncryptor;

@Service
public class EncryptedEmailService {
    
    @Autowired
    private PIEncryptor emailEncryptor;

    @Autowired
    private EncryptedEmailRepository encryptedEmailRepository;

    public String getOriginEmail(String hashEmail){
        EncryptedEmailMap emailMap=encryptedEmailRepository.findByEmailHash(hashEmail);
        if(emailMap==null){
            return null;
        }
        return emailEncryptor.decrypt_AES256(emailMap.getEmailAES256());
    }

    public void setNewEmailMap(String originEmail, String hashEmail){
        EncryptedEmailMap exist=encryptedEmailRepository.findByEmailHash(hashEmail);
        if(exist!=null){
            throw new RuntimeException("DuplicatedEmailMap");
        }
        EncryptedEmailMap emailMap=EncryptedEmailMap.builder()
            .emailAES256(emailEncryptor.encrypt_AES256(originEmail))
            .emailHash(hashEmail)
            .build();
        
        encryptedEmailRepository.save(emailMap);
    }

    public void deleteEmailMap(String hashEmail){
        EncryptedEmailMap emailMap=encryptedEmailRepository.findByEmailHash(hashEmail);
        if(emailMap==null){
            return ;
        }
        encryptedEmailRepository.delete(emailMap);
    }
    
}
