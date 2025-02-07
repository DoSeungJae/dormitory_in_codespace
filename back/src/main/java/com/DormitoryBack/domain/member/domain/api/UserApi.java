package com.DormitoryBack.domain.member.domain.api;

import com.DormitoryBack.domain.member.domain.dto.UserLogInDTO;
import com.DormitoryBack.domain.member.domain.dto.UserRequestDTO;
import com.DormitoryBack.domain.member.domain.dto.UserResponseDTO;
import com.DormitoryBack.domain.member.domain.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"https://improved-space-tribble-vjvwrwx956jh69w4-3000.app.github.dev", "https://turbo-chainsaw-rpvvwx9pp5c5p55-3000.app.github.dev"})
@RestController
@RequestMapping("api/v1/user")
@Slf4j
public class UserApi {
    private final UserService userService;
    @Autowired
    public UserApi(UserService userService){
        this.userService=userService;

    }
    @GetMapping("/test")
    public String userTest(){
        log.info("12");
        return "12";
    }

    /*
    @GetMapping("")
    public ResponseEntity allUsers(){
        List<UserResponseDTO> users=userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);

    }
    */

    @GetMapping("/{usrId}")
    public ResponseEntity user(@PathVariable("usrId") Long usrId){
        UserResponseDTO responseDTO=userService.getUser(usrId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/findByNick/{nickName}")
    public ResponseEntity userByNickName(@PathVariable("nickName") String nickName){
        UserResponseDTO responseDTO=userService.getUserByNickName(nickName);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);

    }

    @GetMapping("/NickName")
    public ResponseEntity userNickName(@RequestHeader("Authorization") String token){
        String userNickName=userService.getUserNickName(token);
        return ResponseEntity.status(HttpStatus.OK).body(userNickName);
    }
    @PostMapping("/logIn")
    public ResponseEntity logIn(@RequestBody UserLogInDTO dto){ //return type : ResponseEntity <String>
        log.info(dto.toString());
        try{
            String token=userService.logIn(dto);
            log.info("로그인 성공");

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(token);
        }
        catch(IllegalArgumentException e){
            log.error("로그인 실패 : "+e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(false);
        }
    }


    @PostMapping("/join")
    public ResponseEntity signUp(@RequestBody UserRequestDTO dto) {
        UserResponseDTO responseDTO = userService.makeNewUser(dto);
        return ResponseEntity.ok().body("회원가입이 성공적으로 처리되었습니다 " + responseDTO.toString());
    }

    //토큰 인증 필요
    @PatchMapping("/{usrId}")
    public ResponseEntity updateUser(@PathVariable("usrId") Long usrId, @RequestBody UserRequestDTO dto){
        UserResponseDTO responseDTO=userService.updateUser(usrId,dto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @DeleteMapping("/{usrId}")
    public ResponseEntity deleteUser(@PathVariable("usrId") Long usrId, @RequestBody UserRequestDTO dto){
        userService.deleteUser(usrId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("UserDeleted");
    }

}
