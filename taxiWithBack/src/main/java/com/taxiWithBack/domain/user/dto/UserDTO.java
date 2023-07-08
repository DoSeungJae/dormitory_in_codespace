package com.taxiWithBack.domain.user.dto;

public class UserDTO {
    public String logInId;
    public String passWord;
    public UserDTO(String logInId, String passWord){
        this.logInId=logInId;
        this.passWord=passWord;
    }
    public UserDTO toEntity(){
        return new UserDTO(logInId,passWord);
    }

    @Override
    public String toString(){
        return "LogInId : "+logInId+" passWord : "+passWord;


    }
}
