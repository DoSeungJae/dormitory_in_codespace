package com.DormitoryBack.exception.globalException;

import com.DormitoryBack.exception.ErrorInfo;

public class EntitiyNotFoundException extends RuntimeException {

    private final ErrorInfo errorInfo;

    public EntitiyNotFoundException(ErrorInfo errorInfo){
        super(errorInfo.getErrorMessage());
        this.errorInfo=errorInfo;
    }

    public EntitiyNotFoundException(ErrorInfo errorInfo, Throwable cause){
        super(errorInfo.getErrorMessage(), cause); // 메시지와 원인 설정
        this.errorInfo = errorInfo; // ErrorInfo 필드 초기화
    }

    public ErrorInfo getErrorInfo(){
        return errorInfo;
    }
}
