package com.DormitoryBack.domain.auth.email.domain.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class EmailRequestDTO {

    String email;

    String code;


}
