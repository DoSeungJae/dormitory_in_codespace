package com.DormitoryBack.infrastructure.kafka.dto.notification;

import com.DormitoryBack.infrastructure.kafka.enums.EntityType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;

@Builder
public class NotificationDto {
    @Enumerated(EnumType.STRING)
    private EntityType subjectType;
    private Long subjectUserId;
    private Long subjectId;
    @Enumerated(EnumType.STRING)
    private EntityType triggerType;
    private Long triggerUserId;
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


