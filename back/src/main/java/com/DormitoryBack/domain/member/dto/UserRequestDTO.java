package com.DormitoryBack.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDTO {

    //데이터 필드 이름을 eMail로 설정하면 에러 발생. 따라서 eMail -> mail, 
    //근데 다른 클래스에서는 eMail로도 되는 경우도 있던데? 왜이럼?
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