package com.DormitoryBack.domain.group.chat.domain.entity;

import com.DormitoryBack.domain.group.chat.domain.enums.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    @Column
    private String room;
    @Column
    private String username;
    @Column
    private String message;
    @Column
    private LocalDateTime createdTime;

}
