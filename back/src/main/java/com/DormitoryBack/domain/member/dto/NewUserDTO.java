package com.DormitoryBack.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDTO {

    String eMail;
    String passWord;
    String nickName;
    Long dormId;

}