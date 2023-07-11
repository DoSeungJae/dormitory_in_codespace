package com.taxiWithBack.domain.user.api;

import com.taxiWithBack.domain.user.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserApi {

    @GetMapping("/test")
    public String test(){
        log.info("12");

        return "12";

    }

    @PostMapping("/logIn")
    public ResponseEntity<UserDTO> logIn(@RequestBody UserDTO dto){ //return type : ResponseEntity <> :User
        log.info("logIn");
        UserDTO user=dto.toEntity();
        log.info(user.toString());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @GetMapping("/signUp/newUser")
    public void signUp(){


    }

}
