package com.DormitoryBack.domain.member.domain.dto;

import com.DormitoryBack.domain.auth.oauth.domain.enums.ProviderType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDTO {

    //데이터 필드 이름을 eMail로 설정하고  getter 호출(eMail getter) 시 에러 발생. 따라서 eMail -> mail로 필드 이름 변경, 
    //근데 다른 클래스에서는 eMail로도 되는 경우도 있던데? 왜이럼?
    private String mail;
    private String passWord;
    private String nickName;
    private String phoneNum;
    private Long dormId;
    private String confirmPassword; //유저 수정의 경우에만 사용됨.

    //아래 데이터 필드들은 OAuth2를 통해 회원가입을 하는 경우에만 해당.
    private ProviderType provider; 

    private String authToken;
    
    public UserRequestDTO(String mail, String passWord, String nickName, Long dormId){
        this.mail=mail;
        this.passWord=passWord;
        this.nickName=nickName;
        this.dormId=dormId;
    }



}