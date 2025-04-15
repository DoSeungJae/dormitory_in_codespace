package com.DormitoryBack.domain.block.exception;

import com.DormitoryBack.exception.ErrorInfo;

public class UnblockableException extends RuntimeException{
    private final ErrorInfo errorInfo;

    public UnblockableException(ErrorInfo errorInfo){
        super(errorInfo.getErrorMessage());
        this.errorInfo=errorInfo;
    }

    public UnblockableException(ErrorInfo errorInfo, Throwable cause){
        super(errorInfo.getErrorMessage(), cause); // 메시지와 원인 설정
        this.errorInfo = errorInfo; // ErrorInfo 필드 초기화
    }

    public ErrorInfo getErrorInfo(){
        return errorInfo;
    }
    
}
