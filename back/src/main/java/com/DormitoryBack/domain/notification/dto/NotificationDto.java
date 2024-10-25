package com.DormitoryBack.domain.notification.dto;

import java.time.LocalDateTime;

//import com.DormitoryBack.global.model.Notifiable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class NotificationDto {
    private Long id;
    private Notifiable subject;
    private Notifiable trigger;
    private String content;
    private Boolean isConfirmed;

    private LocalDateTime triggeredDate;

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
