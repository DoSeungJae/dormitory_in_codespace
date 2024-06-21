package com.DormitoryBack.domain.group.chat.domain.service;

import com.DormitoryBack.domain.group.chat.domain.entity.Message;
import com.DormitoryBack.domain.group.chat.domain.repository.MessageRepository;
import com.DormitoryBack.domain.group.domain.repository.GroupRepository;
import com.DormitoryBack.domain.notification.service.NotificationServiceExternal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageService {
    private final MessageRepository messageRepository;
    public List<Message> getMessages(String room){
        return messageRepository.findAllByRoom(room);
    }

    public Message saveMessage(Message message){
        Message saved=messageRepository.save(message);
        return saved;
    }
}
