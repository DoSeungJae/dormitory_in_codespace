package com.DormitoryBack.domain.member.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.DormitoryBack.domain.member.domain.entity.EncryptedPhoneNumMap;
import com.DormitoryBack.domain.member.domain.repository.EncryptedPhoneNumRepository;
import com.DormitoryBack.module.crypt.PIEncryptor;

@Service
public class EncryptedPhoneNumService {

    @Autowired
    private PIEncryptor phoneNumEncryptor;

    @Autowired
    private EncryptedPhoneNumRepository encryptedPhoneNumRepository;

    public String getOriginPhoneNumber(String numberHash){
        EncryptedPhoneNumMap numberMap=encryptedPhoneNumRepository.findByNumberHash(numberHash);
        if(numberMap==null){
            return null;
        }
        return phoneNumEncryptor.decrypt_AES256(numberMap.getNumberAES256());
    } 

    public void setNewNumberMap(String originNumber, String numberHash){
        EncryptedPhoneNumMap exist=encryptedPhoneNumRepository.findByNumberHash(numberHash);
        if(exist!=null){
            throw new RuntimeException("DuplicatedNumberMap");
        }
        EncryptedPhoneNumMap numberMap=EncryptedPhoneNumMap.builder()
            .numberAES256(phoneNumEncryptor.encrypt_AES256(originNumber))
            .numberHash(numberHash)
            .build();

        encryptedPhoneNumRepository.save(numberMap);
    }

    public void deleteNumberMap(String numberHash){
        EncryptedPhoneNumMap numberMap=encryptedPhoneNumRepository.findByNumberHash(numberHash);
        if(numberMap==null){
            return ;
        }
        encryptedPhoneNumRepository.delete(numberMap);
    }
}
