package com.taxiWithBack.domain.user.service;


import com.taxiWithBack.domain.user.dto.UserDTO;
import com.taxiWithBack.domain.user.entity.User;
import com.taxiWithBack.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User signUp(String eMail, String passWord, String nickName) {
        User existingUser = userRepository.findByeMail(eMail);
        if (existingUser != null) {
            throw new IllegalArgumentException("이미 사용중인 메일입니다.");

        }
        User user = User.builder()
                .eMail(eMail)
                .passWord(passWord)
                .nickName(nickName)
                .build();

        User saved=userRepository.save(user);
        return saved;

    }



    public User logIn(String eMail, String passWord){
        User user=userRepository.findByeMail(eMail);
        if(user==null){
            throw new RuntimeException("해당 이메일을 가진 사용자가 존재하지 않습니다."); // IllegalArgumentException -> Run
        }

        else{
            throw new RuntimeException("비밀번호가 일치하지 않습니다"); //IllegalArgumentException -> Run

        }
    }


}
