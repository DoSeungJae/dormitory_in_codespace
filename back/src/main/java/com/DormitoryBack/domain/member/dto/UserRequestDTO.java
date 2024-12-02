package com.DormitoryBack.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDTO {

    String mail;
    String passWord;
    String nickName;
    Long dormId;

    public UserRequestDTO(String mail, String passWord, String nickName, Long dormId){
        this.mail=mail;
        this.passWord=passWord;
        this.nickName=nickName;
        this.dormId=dormId;
    }



}