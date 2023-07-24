package com.taxiWithBack.domain.user.dto;

public class UserDTO {
    String eMail;
    String passWord;
    String nickName;

    public UserDTO(String eMail, String passWord,String nickName){
        this.eMail=eMail;
        this.passWord=passWord;
        this.nickName=nickName;
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


    /*
    public UserDTO toEntity(){
        return new UserDTO(eMail,passWord,nickName);
    }

    @Override
    public String toString(){
        String str="eMail : "+eMail+" passWord : "+passWord;
        if(nickName!=null){
            return str+" nickName : "+nickName;
        }
        return str;


    }

     */
}