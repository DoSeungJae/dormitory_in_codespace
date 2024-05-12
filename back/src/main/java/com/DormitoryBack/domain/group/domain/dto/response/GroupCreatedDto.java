package com.DormitoryBack.domain.group.domain.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class GroupCreatedDto {
    private Long id;
    private Long dormId;
    private Long hostId;
    private Long articleId;
    private String category;
    private Long maxCapacity;
    private Boolean isProceeding;
    private LocalDateTime createdTime;
    private Long currentNumberOfMembers;

    public String toJsonString(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }


}
