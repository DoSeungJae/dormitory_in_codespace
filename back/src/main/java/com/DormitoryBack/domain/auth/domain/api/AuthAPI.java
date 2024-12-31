package com.DormitoryBack.domain.auth.domain.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DormitoryBack.domain.auth.domain.dto.GoogleRequestDTO;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = {"https://improved-space-tribble-vjvwrwx956jh69w4-3000.app.github.dev", "https://turbo-chainsaw-rpvvwx9pp5c5p55-3000.app.github.dev"})
@RestController
@RequestMapping("api/v1/auth")
@Slf4j
public class AuthAPI {

    public ResponseEntity googleAuth(@RequestBody GoogleRequestDTO dto){
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
