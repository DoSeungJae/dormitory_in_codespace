package com.DormitoryBack.domain.member.restriction.domain.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter //테스트 환경에서만 이 어노테이션이 동작하게 할 수 없을까?
public class RestrictionResponseDTOList {

    int number;

    List<RestrictionResponseDTO> dtoList;

    
}
