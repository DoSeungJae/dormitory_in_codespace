package com.taxiWithBack.domain.user.dto;

public class UserDTO {
    public Long id;
    public String eMail;
    public String passWord;

    public UserDTO(Long id, String eMail, String passWord){
        this.id=id;
        this.eMail=eMail;
        this.passWord=passWord;

    }

    public UserDTO toEntity(){
        return new UserDTO(id,eMail,passWord);

    }
}
