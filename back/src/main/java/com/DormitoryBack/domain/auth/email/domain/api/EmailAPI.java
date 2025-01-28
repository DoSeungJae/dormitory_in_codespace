package com.DormitoryBack.domain.auth.email.domain.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DormitoryBack.domain.auth.email.domain.dto.EmailRequestDTO;
import com.DormitoryBack.domain.auth.email.domain.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = {"https://improved-space-tribble-vjvwrwx956jh69w4-3000.app.github.dev", "https://turbo-chainsaw-rpvvwx9pp5c5p55-3000.app.github.dev"})
@RestController
@RequestMapping("api/v1/email")
@Slf4j
public class EmailAPI {

    @Autowired
    private EmailService emailService;

    @PostMapping("/verifyCode")
    public ResponseEntity mailConfirm(@RequestBody EmailRequestDTO dto) throws Exception{
        String code=emailService.sendVerifyMail(dto);
        return ResponseEntity.status(HttpStatus.OK).body(code);
    }

    
    
}
