package com.DormitoryBack.domain.member.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.oauth.domain.enums.ProviderType;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByNickName(String nickName);

    User findByEncryptedEmail(String eMail);

    User findByEncryptedEmailAndProviderIsNull(String email);
    
    User findByEncryptedPhoneNum(String phoneNum);

    User findByProviderAndEncryptedEmail(ProviderType provider, String encryptedEmail);


}