package com.DormitoryBack.domain.member.domain.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class UserLogInDTO {
    private String eMail;
    private String passWord;
    public UserLogInDTO(String eMail, String passWord){
        this.eMail=eMail;
        this.passWord=passWord;
    }


}
