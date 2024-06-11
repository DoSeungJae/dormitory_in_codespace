package com.DormitoryBack.domain.notification.dto;

import com.DormitoryBack.domain.notification.enums.EntityType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class NotificationDto {
    @Enumerated(EnumType.STRING)
    private EntityType subjectType;
    private String subject;
    @Enumerated(EnumType.STRING)
    private EntityType triggerType;
    private String trigger;

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
