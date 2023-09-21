package com.taxiWithBack.domain.member.dto;

import java.util.Set;

public class UserDTO {

    String eMail;
    String passWord;
    String nickName;

    Set<String> roles;
    public UserDTO(String eMail, String passWord,String nickName){
        this.eMail=eMail;
        this.passWord=passWord;
        this.nickName=nickName;
    }

    public UserDTO(String eMail, String passWord,String nickName,Set<String> roles){
        this.roles=roles;
    }
    @Override
    public String toString(){
        return (this.eMail+","+this.passWord+","+this.nickName+","+this.roles);

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

    public Set<String> getRoles() {
        return roles;
    }
    public void setRoles(Set<String> roles){
        this.roles=roles;
    }

}