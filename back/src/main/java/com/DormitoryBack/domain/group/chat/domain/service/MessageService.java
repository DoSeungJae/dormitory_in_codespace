package com.DormitoryBack.domain.group.chat.domain.service;

import com.DormitoryBack.domain.group.chat.domain.entity.Message;
import com.DormitoryBack.domain.group.chat.domain.repository.MessageRepository;
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
        return messageRepository.save(message);
    }
}
