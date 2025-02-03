package com.DormitoryBack.domain.member.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceExternal {

    @Autowired
    UserRepository userRepository;

        public Boolean isUserExist(Long userId){
        User user=userRepository.findById(userId).orElse(null); //repository에 새로운 메서드 선언 후 리펙터링해야함
        if(user==null){
            return false;
        }
        return true;
    }
}
