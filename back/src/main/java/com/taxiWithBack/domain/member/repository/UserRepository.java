package com.taxiWithBack.domain.member.repository;


import com.taxiWithBack.domain.member.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByNickName(String nickName);

    User findByeMail(String eMail);

    User findByUsrId(Long usrId);


}