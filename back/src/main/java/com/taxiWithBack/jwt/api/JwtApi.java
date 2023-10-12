package com.taxiWithBack.jwt.api;


import com.taxiWithBack.domain.member.service.UserService;
import com.taxiWithBack.jwt.TokenProvider;
import com.taxiWithBack.jwt.dto.JwtDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://localhost:3000")
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
    @PostMapping("/validate") //boolean -> Boolean
    public ResponseEntity<Boolean> tokenValidation(@RequestBody JwtDTO dto){
        log.info(dto.getToken());
        Boolean isValid=jwtService.validateToken(dto.getToken());
        if(isValid==true){
            return ResponseEntity.ok(isValid);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
