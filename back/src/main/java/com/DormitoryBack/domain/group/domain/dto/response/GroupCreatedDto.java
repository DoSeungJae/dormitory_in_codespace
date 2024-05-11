package com.DormitoryBack.domain.group.domain.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GroupCreatedDto {
    private Long id;
    private Long dormId;
    private Long hostId;
    private Long articleId;
    private String category;
    private Long maxCapacity;


}
