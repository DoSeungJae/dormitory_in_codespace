package com.DormitoryBack.domain.group.chat.domain.service;

import com.DormitoryBack.domain.group.chat.domain.entity.Message;
import com.DormitoryBack.domain.group.chat.domain.repository.MessageRepository;
import com.DormitoryBack.domain.group.domain.entitiy.Group;
import com.DormitoryBack.domain.group.domain.repository.GroupRepository;
import com.DormitoryBack.domain.notification.dto.Notifiable;
import com.DormitoryBack.domain.notification.enums.EntityType;
import com.DormitoryBack.domain.notification.service.NotificationServiceExternal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageService {
    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;
    private final NotificationServiceExternal notificationService;

    public List<Message> getMessages(String room){
        return messageRepository.findAllByRoom(room);
    }

    public Message saveMessage(Message message){
        Message saved=messageRepository.save(message);

        Group group=groupRepository.findById(Long.valueOf(message.getRoom())).orElse(null);

        Notifiable subject=Notifiable.builder()
                .entityType(EntityType.GROUP)
                .entityId(Long.valueOf(message.getRoom()))
                .stringifiedEntity(group.toJsonString())
                .build();

        Notifiable trigger=Notifiable.builder()
                .entityType(EntityType.MESSAGE)
                .entityId(Long.valueOf(saved.getId()))
                .stringifiedEntity(message.toJsonString())
                .build();

        notificationService.saveAndPublishNotification(subject,trigger,saved.getMessage());

        return saved;
    }
}
