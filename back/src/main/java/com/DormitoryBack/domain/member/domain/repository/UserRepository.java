package com.DormitoryBack.domain.member.domain.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.DormitoryBack.domain.member.domain.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByNickName(String nickName);

    User findByEncryptedEmail(String eMail); //테스트 필요 

}