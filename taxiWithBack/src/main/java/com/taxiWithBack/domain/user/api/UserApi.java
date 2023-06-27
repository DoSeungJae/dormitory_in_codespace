package com.taxiWithBack.domain.user.api;


import com.taxiWithBack.domain.user.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public void logIn(@RequestBody UserDTO dto){ //return type : ResponseEntity
        UserDTO user=dto.toEntity();
        log.info(user.toString());

    }

    @GetMapping("signUp")
    public void logIn(){
        
    }

}
