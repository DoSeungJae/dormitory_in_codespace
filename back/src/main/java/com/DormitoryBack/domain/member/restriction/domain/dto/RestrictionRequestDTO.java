package com.DormitoryBack.domain.member.restriction.domain.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestrictionRequestDTO {
    
    private String accessKey;
    private Long userId;
    private String reason;
    private Long durationDays;

    public void setAccessKey(String accessKey){
        this.accessKey=accessKey;
    }
}

