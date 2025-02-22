package com.DormitoryBack.domain.member.restriction.domain.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RestrictionResponseDTO {
    
    private Long userId;

    private LocalDateTime expireTime;

    private Boolean isExpired;

    private String reason;


}
