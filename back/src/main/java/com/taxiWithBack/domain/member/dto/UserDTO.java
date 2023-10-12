package com.taxiWithBack.domain.member.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class UserDTO {

    String eMail;
    String passWord;
    String nickName;

    public UserDTO(){}

    public UserDTO(String eMail, String passWord,String nickName){
        this.eMail=eMail;
        this.passWord=passWord;
        this.nickName=nickName;
    }

    @Override
    public String toString(){
        return (this.eMail+","+this.passWord+","+this.nickName+",");
    }

    public String getEMail(){
        return eMail;
    }

    public void setEMail(String eMail){
        this.eMail=eMail;
    }

    public String getPassWord(){
        return passWord;
    }

    public void setPassWord(String passWord){
        this.passWord=passWord;

    }
    public String getNickName(){
        return nickName;
    }

    public void setNickName(String nickName){
        this.nickName=nickName;
    }
}