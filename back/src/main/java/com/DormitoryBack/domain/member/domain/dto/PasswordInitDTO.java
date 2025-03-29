package com.DormitoryBack.domain.member.domain.dto;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder //테스트용
public class PasswordInitDTO {

    @NotNull
    String email;

    @NotNull
    String emailToken;

    @NotNull
    String newPassword;

    
}
