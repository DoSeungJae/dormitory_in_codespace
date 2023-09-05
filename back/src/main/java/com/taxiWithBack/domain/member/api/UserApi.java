package com.taxiWithBack.domain.member.api;

import com.taxiWithBack.domain.member.dto.UserDTO;
import com.taxiWithBack.domain.member.entity.User;
import com.taxiWithBack.domain.member.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public String test(){
        log.info("12");
        return "12";
    }


    @PostMapping("/logIn")
    public ResponseEntity<UserDTO> logIn(@RequestBody UserDTO dto){ //return type : ResponseEntity <> :User
        try{
            User user=userService.logIn(dto.getEmail(),dto.getPassWord());
            log.info("로그인 성공");

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(dto);
        }
        catch(IllegalArgumentException e){
            log.error("로그인 실패 : "+e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

    @PostMapping("/join") //singIn ->= join !!
    public ResponseEntity<?> signUp(@RequestBody UserDTO dto) {
        log.info(dto.toString());

        User user = userService.signUp(dto.getEmail(), dto.getPassWord(), dto.getNickName());
        return ResponseEntity.ok().body("회원가입이 성공적으로 처리되었습니다 " + user.toString());
    }


}
