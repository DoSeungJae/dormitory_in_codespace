package com.DormitoryBack.domain.oauth.domain.dto;

import com.DormitoryBack.domain.oauth.domain.enums.ProviderType;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GoogleResponseDTO {

    private Boolean isValid;
    
    private ProviderType provider;

    private String email;
}
