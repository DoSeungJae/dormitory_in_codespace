package com.taxiWithBack.domain.user.api;

import com.taxiWithBack.domain.user.dto.UserDTO;
import com.taxiWithBack.domain.user.entity.User;
import com.taxiWithBack.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("api/vi/user")
@Slf4j
public class UserApi {
    private UserService userService;
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

    @PostMapping("/join") //singIn -> join !!
    public ResponseEntity<?> signUp(@RequestBody UserDTO dto) {
        User user = userService.signUp(dto.getEmail(), dto.getPassWord(), dto.getNickName());
        return ResponseEntity.ok().body("회원가입이 성공적으로 처리되었습니다 " + user.toString());
    }


}
