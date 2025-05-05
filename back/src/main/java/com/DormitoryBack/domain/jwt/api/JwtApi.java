package com.DormitoryBack.domain.jwt.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.jwt.dto.JwtDTO;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = {"https://improved-space-tribble-vjvwrwx956jh69w4-3000.app.github.dev", "https://turbo-chainsaw-rpvvwx9pp5c5p55-3000.app.github.dev"})
@RestController
@RequestMapping("api/v1/token")
@Slf4j
public class JwtApi {
    private final TokenProvider jwtService;
    @Autowired
    public JwtApi(TokenProvider jwtService){
        this.jwtService=jwtService;

    }
    @GetMapping("/test")
    public String JwtTest(){
        String message="Test in JWT";
        log.info(message);
        return message;

    }

    @PostMapping("/validate") //<boolean>(primitive) -> <Boolean>(wrapper) ResponseEntity의 body에 primitive type을 담을 수 없음.
    public ResponseEntity<Boolean> tokenValidation(@RequestBody JwtDTO dto){
        Boolean isValid=jwtService.validateToken(dto.getToken());
        return ResponseEntity.status(HttpStatus.OK).body(isValid);
    }

    @GetMapping("/userId")
    public ResponseEntity<?> fromTokenToUserId(@RequestHeader("Authorization") String token){
        Boolean isValid=jwtService.validateToken(token);
        if(!isValid){
            ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jwtService.getUserIdFromToken(token));
    }

}
