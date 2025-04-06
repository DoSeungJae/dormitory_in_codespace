package com.DormitoryBack.domain.notification.entity;

import com.DormitoryBack.domain.notification.dto.Notifiable;
import com.DormitoryBack.domain.notification.enums.EntityType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Getter
@Data
public class Notification {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EntityType subjectType;

    @NotNull
    private Long subjectId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private EntityType triggerType;

    @NotNull
    private Long triggerId;

    @NotNull
    private String triggerContent;

    @CreatedDate
    @NotNull
    private LocalDateTime triggeredDate;

    @NotNull
    private Boolean isConfirmed;
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

    public void confirm(){
        isConfirmed=true;
    }

}
