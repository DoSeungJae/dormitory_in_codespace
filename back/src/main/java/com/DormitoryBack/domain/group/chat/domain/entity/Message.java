package com.DormitoryBack.domain.group.chat.domain.entity;

import com.DormitoryBack.domain.group.chat.domain.enums.MessageType;
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

}
