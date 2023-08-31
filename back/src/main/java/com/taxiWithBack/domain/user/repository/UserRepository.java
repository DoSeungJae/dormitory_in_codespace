package com.taxiWithBack.domain.user.repository;


import com.taxiWithBack.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User,Long> {
    User findByeMail(String eMail);
    User findByNickName(String nickName);


}