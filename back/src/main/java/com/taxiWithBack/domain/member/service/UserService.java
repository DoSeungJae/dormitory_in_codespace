package com.taxiWithBack.domain.member.service;


import com.taxiWithBack.domain.member.entity.User;
import com.taxiWithBack.domain.member.repository.UserRepository;
import com.taxiWithBack.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
//Spring Security
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired

    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    //@Autowired
    //private AuthenticationManager authenticationManager1;



    public User signUp(String eMail, String passWord, String nickName) {
        User existingUserMail = userRepository.findByeMail(eMail);
        User existingUserNick = userRepository.findByNickName(nickName);

        if (existingUserMail != null) {
            throw new IllegalArgumentException("이미 사용중인 메일입니다.");
        }

        if(existingUserNick != null){
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        User user = User.builder()
                .eMail(eMail)
                .passWord(passWord)
                .nickName(nickName)
                .build();

        User saved=userRepository.save(user);
        return saved;

    }

    public String logInNoSecurity(String eMail, String passWord){
        User user=userRepository.findByeMail(eMail);
        if(user==null){
            throw new RuntimeException("해당 이메일을 가진 사용자가 존재하지 않습니다."); // IllegalArgumentException -> Run
        }

        else if(!user.getPassWord().equals(passWord)){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return tokenProvider.createTokenNoSecurity(user);


    }


}
