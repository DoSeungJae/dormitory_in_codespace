package com.DormitoryBack.domain.jwt.dto;

public class JwtDTO {
    private String token;

    public String getToken(){
        return this.token;
    }
    public void setToken(String newToken){
        this.token=newToken;
    }

}
