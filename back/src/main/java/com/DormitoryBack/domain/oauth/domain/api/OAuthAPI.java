package com.DormitoryBack.domain.oauth.domain.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DormitoryBack.domain.oauth.domain.dto.GoogleRequestDTO;
import com.DormitoryBack.domain.oauth.domain.service.OAuthService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = {"https://improved-space-tribble-vjvwrwx956jh69w4-3000.app.github.dev", "https://turbo-chainsaw-rpvvwx9pp5c5p55-3000.app.github.dev"})
@RestController
@RequestMapping("api/v1/oauth")
@Slf4j
public class OAuthAPI {

    @Autowired
    private OAuthService authService;

    @PostMapping("/google")
    public ResponseEntity googleAuth(@RequestBody GoogleRequestDTO dto){
        String email=authService.signUpWithGoogle(dto); //임시 로직
        return ResponseEntity.status(HttpStatus.OK).body(email);
    }
}
