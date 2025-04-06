package com.DormitoryBack.domain.jwt.exception;

import com.DormitoryBack.exception.ErrorInfo;

public class JwtException extends RuntimeException {
    
    private final ErrorInfo errorInfo;

    public JwtException(ErrorInfo errorInfo){
        super(errorInfo.getErrorMessage());
        this.errorInfo=errorInfo;
    }

    public JwtException(ErrorInfo errorInfo, Throwable cause){
        super(errorInfo.getErrorMessage(), cause); // 메시지와 원인 설정
        this.errorInfo = errorInfo; // ErrorInfo 필드 초기화
    }

    public ErrorInfo getErrorInfo(){
        return errorInfo;
    }

}
