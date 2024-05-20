package com.DormitoryBack.domain.group.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SingleGroupDto {
    private Long id;
    private Long dormId;
    private Long hostId;
    private LocalDateTime createdTime;
    private String category;
    private Long maxCapacity;
    private Boolean isProceeding;
    private Long currentNumberOfMembers;
    private List<String> members;
    //UserResponseDto -> String

}
