package com.DormitoryBack.domain.auth.adminKey.service;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import com.DormitoryBack.domain.auth.adminKey.exception.AccessDeniedException;
import com.DormitoryBack.exception.ErrorInfo;
import com.DormitoryBack.exception.ErrorType;

@Service
public class AdminKeyValidator {
    
    @Value("${administrator.key}")
    private String adminKey;

    public void validateAdminKeyOrThrow(String adminKey){
        if(adminKey==null || !this.adminKey.equals(adminKey)){
            throw new AccessDeniedException(new ErrorInfo(ErrorType.AccessDenied, "유효하지 않은 관리자 키입니다."));
        }
        return ;
    }

}
