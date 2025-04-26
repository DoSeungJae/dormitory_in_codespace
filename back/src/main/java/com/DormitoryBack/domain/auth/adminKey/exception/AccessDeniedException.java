package com.DormitoryBack.domain.auth.adminKey.exception;

import com.DormitoryBack.exception.ErrorInfo;

public class AccessDeniedException extends RuntimeException {
    
    private final ErrorInfo errorInfo;

    public AccessDeniedException(ErrorInfo errorInfo){
        super(errorInfo.getErrorMessage());
        this.errorInfo=errorInfo;
    }

    public AccessDeniedException(ErrorInfo errorInfo, Throwable cause){
        super(errorInfo.getErrorMessage(), cause); 
        this.errorInfo = errorInfo; 
    }

    public ErrorInfo getErrorInfo(){
        return errorInfo;
    }

}
