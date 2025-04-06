package com.DormitoryBack.domain.auth.adminKey.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class AdminKeyValidator {
    
    @Value("${administrator.key}")
    private String adminKey;

    public void validateAdminKeyOrThrow(String adminKey){
        if(adminKey==null || !this.adminKey.equals(adminKey)){
            throw new AccessDeniedException("AccessDenied: 유효하지 않은 관리자 키입니다.");
        }
        return ;
    }

}
