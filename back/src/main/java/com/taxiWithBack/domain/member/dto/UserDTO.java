package com.taxiWithBack.domain.member.dto;

public class UserDTO {

    String eMail;
    String passWord;
    String nickName;

    public UserDTO(String eMail, String passWord,String nickName){
        this.eMail=eMail;
        this.passWord=passWord;
        this.nickName=nickName;
    }
    @Override
    public String toString(){
        return (this.eMail+","+this.passWord+","+this.nickName);

    }

    public String getEmail(){
        return eMail;
    }

    public void setEmail(String eMail){
        this.eMail=eMail;
    }

    public String getPassWord(){
        return passWord;
    }

    public void setPassWord(){
        this.passWord=passWord;

    }

    public String getNickName(){
        return nickName;
    }

    public void setNickName(String nickName){
        this.nickName=nickName;
    }


}