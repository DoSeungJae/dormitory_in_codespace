package com.taxiWithBack.domain.member.api;

import com.taxiWithBack.domain.member.dto.UserDTO;
import com.taxiWithBack.domain.member.dto.UserLogInDTO;
import com.taxiWithBack.domain.member.entity.User;
import com.taxiWithBack.domain.member.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins="http://localhost:3000")
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

    @GetMapping("")
    public ResponseEntity allUsers(){
        List<User> users=userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);

    }

    @GetMapping("/{usrId}")
    public ResponseEntity user(@PathVariable("usrId") Long usrId){
        User user=userService.getUser(usrId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    @PostMapping("/logIn")
    public ResponseEntity<String> logIn(@RequestBody UserLogInDTO dto){ //return type : ResponseEntity <String>
        log.info(dto.toString());
        try{
            String token=userService.logInNoSecurity(dto.getEMail(),dto.getPassWord());
            log.info("로그인 성공");

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(token);
        }
        catch(IllegalArgumentException e){
            log.error("로그인 실패 : "+e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> signUp(@RequestBody UserDTO dto) {
        log.info(dto.toString());

        User user = userService.signUp(dto.getEMail(), dto.getPassWord(), dto.getNickName());
        return ResponseEntity.ok().body("회원가입이 성공적으로 처리되었습니다 " + user.toString());
    }

    @PatchMapping("/{usrId}")
    public ResponseEntity updateUser(@PathVariable("usrId") Long usrId, @RequestBody UserDTO dto){
        User user=userService.updateUser(usrId,dto);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping("/{usrId}")
    public ResponseEntity deleteUser(@PathVariable("usrId") Long usrId){
        userService.deleteUser(usrId);
        return ResponseEntity.status(HttpStatus.OK).body("USER DELETED");
    }




}
