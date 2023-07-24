package com.taxiWithBack.domain.user.service;


import com.taxiWithBack.domain.user.dto.UserDTO;
import com.taxiWithBack.domain.user.entity.User;
import com.taxiWithBack.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User signIn(String eMail, String passWord, String nickName){
        User existingUser = userRepository.findByeMail(eMail);
        if(existingUser != null){
            throw new IllegalArgumentException("이미 사용중인 메일입니다.");

        }

        String encodedPassword= passwordEncoder.encode(passWord);

        User newUser=new User(eMail,encodedPassword,nickName);

        return userRepository.save(newUser);
    }

    public User logIn(String eMail, String passWord){
        User user=userRepository.findByeMail(eMail);
        if(user==null){
            throw new IllegalArgumentException("해당 이메일을 가진 사용자가 존재하지 않습니다.");
        }

        if(passwordEncoder.matches(passWord,user.getPassWord())){
            return user;
        }
        else{
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");

        }
    }


}
