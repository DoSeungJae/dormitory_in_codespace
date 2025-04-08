package com.DormitoryBack.exception;

import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.DormitoryBack.domain.auth.adminKey.exception.AccessDeniedException;
import com.DormitoryBack.exception.globalException.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> jwtExceptionHandler(JwtException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(com.DormitoryBack.domain.jwt.exception.JwtException.class)
    public ResponseEntity<ErrorInfo> jwtExceptionHandler(com.DormitoryBack.domain.jwt.exception.JwtException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getErrorInfo());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorInfo> accessDeniedExceptionHandler(AccessDeniedException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getErrorInfo());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorInfo> entityNotFoundExceptionHandler(EntityNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getErrorInfo());
    }


}
