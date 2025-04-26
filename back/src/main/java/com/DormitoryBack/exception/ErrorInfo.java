package com.DormitoryBack.exception;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorInfo {

    @Enumerated(EnumType.STRING)
    private ErrorType errorType;
    
    private String errorMessage;
}
