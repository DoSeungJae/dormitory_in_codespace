package com.DormitoryBack.domain.group.chat.domain.entity;

import com.DormitoryBack.domain.group.chat.domain.enums.MessageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("message")
public class Message {
    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    private String room;
    private String username;
    private String message;
    private LocalDateTime createdTime;

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
