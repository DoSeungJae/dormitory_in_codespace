package com.taxiWithBack.domain.member.repository;


import com.taxiWithBack.domain.member.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User,Long> {
    User findByeMail(String eMail);
    User findByNickName(String nickName);


}