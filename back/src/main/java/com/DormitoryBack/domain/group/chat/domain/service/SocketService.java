package com.DormitoryBack.domain.group.chat.domain.service;

import com.DormitoryBack.domain.group.chat.domain.entity.Message;
import com.DormitoryBack.domain.group.chat.domain.enums.MessageType;
import com.DormitoryBack.domain.group.chat.domain.module.ChatManager;
import com.DormitoryBack.domain.group.domain.entitiy.Group;
import com.DormitoryBack.domain.group.domain.repository.GroupRepository;
import com.DormitoryBack.module.TimeOptimizer;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {
    private final MessageService messageService;
    private final GroupRepository groupRepository;
    private final RedisTemplate<String,Long> redisTemplate;
    private final ChatManager chatManager;

    public void sendSocketMessage(Message message, String room) {
        SetOperations<String,Long> setOperations=redisTemplate.opsForSet();
        Set<Long> members= setOperations.members(room);
        Iterator<Long> iterator=members.iterator();
        while (iterator.hasNext()){
            Long memberId= iterator.next();
            SocketIOClient socketClient=chatManager.getSocketClientByUserId(memberId);
            if(socketClient==null){
                return ;
            }
            socketClient.sendEvent("readMessage", message);
        }
    }
    public void saveMessage(Message message) {
        Message storedMessage = messageService.saveMessage(
                Message.builder()
                        .messageType(MessageType.CLIENT)
                        .message(message.getMessage())
                        .room(message.getRoom())
                        .username(message.getUsername())
                        //.createdTime(LocalDateTime.now())
                        .createdTime(TimeOptimizer.now())
                        .build()
        );
        storedMessage.setCreatedTime(null);
        sendSocketMessage(storedMessage, message.getRoom());
    }
    public void saveInfoMessage(String message, String room) {
        Message storedMessage = messageService.saveMessage(
                Message.builder()
                        .messageType(MessageType.SERVER)
                        .message(message)
                        .room(room)
                        //.createdTime(LocalDateTime.now())
                        .createdTime(TimeOptimizer.now())
                        .build()
        );
        storedMessage.setCreatedTime(null);
        sendSocketMessage(storedMessage, room);
    }
}