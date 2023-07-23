package com.taxiWithBack.domain.user.dto;

public class UserDTO {
    String eMail;
    String passWord;
    String nickName;


    /*
    public UserDTO(String eMail, String passWord,){
        this.eMail=eMail;
        this.passWord=passWord;
        this.nickName=null;
    }


     */

    public UserDTO(String eMail, String passWord,String nickName){
        this.eMail=eMail;
        this.passWord=passWord;
        this.nickName=nickName;
    }

    public UserDTO toEntity(){
        /*
        if(nickName!=null){
            return new UserDTO(eMail,passWord,nickName);

        }

         */
        return new UserDTO(eMail,passWord,nickName);
        //return new UserDTO(eMail,passWord);
    }

    @Override
    public String toString(){
        String str="eMail : "+eMail+" passWord : "+passWord;
        if(nickName!=null){
            return str+" nickName : "+nickName;
        }
        return str;


    }
}