package com.DormitoryBack.domain.auth.email.domain.dto;

import com.DormitoryBack.domain.auth.email.domain.enums.CodeStateType;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailResponseDTO {
    CodeStateType stateType;
}
