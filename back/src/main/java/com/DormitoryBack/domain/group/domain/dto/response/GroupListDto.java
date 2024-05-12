package com.DormitoryBack.domain.group.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GroupListDto {
    private Long numberOfGroup;
    private List<String> groups;
    //stringify된 GroupCreatedDto




}
