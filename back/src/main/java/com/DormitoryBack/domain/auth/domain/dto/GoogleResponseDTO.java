package com.DormitoryBack.domain.auth.domain.dto;

import com.DormitoryBack.domain.auth.domain.enums.ProviderType;
import com.DormitoryBack.domain.auth.domain.enums.StateType;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GoogleResponseDTO {

    private StateType state;
    
    private ProviderType provider;

    private String email;

    private String token;
}
