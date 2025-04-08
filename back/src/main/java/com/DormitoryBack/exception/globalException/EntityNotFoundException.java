package com.DormitoryBack.exception.globalException;

import com.DormitoryBack.exception.ErrorInfo;

public class EntityNotFoundException extends RuntimeException {

    private final ErrorInfo errorInfo;

    public EntityNotFoundException(ErrorInfo errorInfo){
        super(errorInfo.getErrorMessage());
        this.errorInfo=errorInfo;
    }

    public EntityNotFoundException(ErrorInfo errorInfo, Throwable cause){
        super(errorInfo.getErrorMessage(), cause); // 메시지와 원인 설정
        this.errorInfo = errorInfo; // ErrorInfo 필드 초기화
    }

    /*
    public ErrorInfo getErrorInfo(){
        return errorInfo;
    }
    */

    public ErrorInfo getErrorInfo(){
        return errorInfo;
    }
}
