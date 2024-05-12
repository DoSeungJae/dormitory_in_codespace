package com.DormitoryBack.domain.member.service;


import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.dto.UserDTO;
import com.DormitoryBack.domain.member.dto.UserResponseDTO;
import com.DormitoryBack.domain.member.repository.UserRepository;
import com.DormitoryBack.domain.member.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//Spring Security
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<UserResponseDTO> getAllUsers(){
        List<User> users=userRepository.findAll();
        if(users.isEmpty()){
            throw new RuntimeException("사용자가 존재하지 않습니다.");
        }
        List<UserResponseDTO> responseDTO = users.stream()
                .map(user -> new UserResponseDTO(user.getId(),user.getEMail(), user.getNickName()))
                .collect(Collectors.toList());

        return responseDTO;
    }

    public UserResponseDTO getUser(Long usrId){
        User user=userRepository.findById(usrId).orElse(null);
        if(user==null){
            throw new IllegalArgumentException("해당 아이디에 대한 사용자가 존재하지 않습니다.");
        }
        UserResponseDTO responseDTO=UserResponseDTO.builder()
                .eMail(user.getEMail())
                .nickName(user.getNickName())
                .build();

        return responseDTO;
    }

    public UserResponseDTO updateUser(Long usrId, UserDTO dto){
        User user=userRepository.findById(usrId).orElse(null);
        if(user==null){
            throw new IllegalArgumentException("해당 아이디에 대한 사용자가 존재하지 않습니다.");
        }
        user.update(dto);
        User saved=userRepository.save(user);
        UserResponseDTO responseDTO=UserResponseDTO.builder()
                .eMail(saved.getEMail())
                .nickName(saved.getNickName())
                .build();

        return responseDTO;

    }

    public UserResponseDTO signUp(String eMail, String passWord, String nickName) {
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
        UserResponseDTO responseDTO=UserResponseDTO.builder()
                .eMail(saved.getEMail())
                .nickName(saved.getNickName())
                .build();

        return responseDTO;

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

    public void deleteUser(Long usrId){
        User target=userRepository.findById(usrId).orElse(null);
        if(target==null){
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
        userRepository.delete(target);
    }


}
