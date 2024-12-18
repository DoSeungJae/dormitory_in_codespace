package com.DormitoryBack.domain.group.domain.dto.response;

import com.DormitoryBack.domain.member.domain.dto.UserResponseDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroupChangedDto {
    private String mode;
    //join or exit

    private UserResponseDTO userChanges;
    //나가거나 들어온 user의 정보

    private long numberOfRemainings;


}
