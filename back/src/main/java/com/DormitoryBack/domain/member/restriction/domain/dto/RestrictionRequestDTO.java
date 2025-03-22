package com.DormitoryBack.domain.member.restriction.domain.dto;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestrictionRequestDTO {
    
    @NotNull
    private String accessKey;

    @NotNull
    private Long userId;

    @NotNull
    private String reason;

    @NotNull
    private Long durationDays;

    public void setAccessKey(String accessKey){
        this.accessKey=accessKey;
    }
}

