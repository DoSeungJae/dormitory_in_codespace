package com.DormitoryBack.domain.member.repository;


import com.DormitoryBack.domain.member.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByNickName(String nickName);

    User findByeMail(String eMail);

}