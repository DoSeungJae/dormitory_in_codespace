package com.DormitoryBack.domain.notification.service;

import com.DormitoryBack.domain.article.domain.repository.ArticleRepository;
import com.DormitoryBack.domain.group.domain.repository.GroupRepository;
import com.DormitoryBack.domain.member.repository.UserRepository;
import com.DormitoryBack.domain.notification.module.NotificationSocketManager;
import com.corundumstudio.socketio.SocketIOClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class NotificationSocketService {
    private final NotificationSocketManager socketManager;
    private final ObjectMapper objectMapper=new ObjectMapper();
    private final RedisTemplate<String,Long> redisTemplate;
    public void sendNotification(String message){
        //subjectType이 group인 경우 member 모두에게 알림 전송
        String subjectType;
        Long subjectUserId;
        Long triggerUserId;
        try{
            JsonNode messageNode=objectMapper.readTree(message);
            subjectType=messageNode.get("subject").get("entityType").asText();
            subjectUserId=getUserIdFromEntity(messageNode.get("subject"));
            triggerUserId=getUserIdFromEntity(messageNode.get("trigger"));

        }catch(JsonProcessingException e){
            throw new RuntimeException(e.getMessage());
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
        if(subjectType=="GROUP"){
            String groupId;
            try{
                JsonNode messageNode=objectMapper.readTree(message);
                groupId=messageNode.get("subject").get("stringifiedEntity").get("id").asText();
            }catch(JsonProcessingException e){
                throw new RuntimeException(e.getMessage());
            }catch(Exception e){
                throw new RuntimeException(e.getMessage());
            }

            SetOperations<String,Long> setOperations=redisTemplate.opsForSet();
            Set<Long> membersId=setOperations.members(groupId);
            Iterator<Long> iterator=membersId.iterator();
            while(iterator.hasNext()){
                Long memberId=iterator.next();
                SocketIOClient targetSocketClient=socketManager.getSocketClientByUserId(memberId);
                targetSocketClient.sendEvent("notification",message);
            }
        }
        else{
            if(subjectUserId==triggerUserId){
                return ;
            }
            SocketIOClient targetSocketClient=socketManager.getSocketClientByUserId(subjectUserId);
            targetSocketClient.sendEvent("notification",message);
        }
    }
    private Long getUserIdFromEntity(JsonNode entityNode){
        String entityType=entityNode.get("entityType").asText();
        JsonNode entity=entityNode.get("stringifiedEntity");
        log.info("{}",entity);
        if(entity.isTextual()){
            try{
                log.info("12");
                entity=objectMapper.readTree(entity.asText());
            }catch (Exception e){
                log.info(e.getMessage());
            }
        }
        Long userId=-1L;
        try{
            switch (entityType){
                case "ARTICLE":
                    userId=entity.get("userId").asLong();
                    break;
                case "COMMENT":
                    userId=entity.get("user").get("id").asLong();
                    break;
                case "GROUP":
                    userId=entity.get("hostId").asLong();
                    break;
                case "USER":
                    userId=entity.get("id").asLong();
                    break;
                default:
                    userId=-1L;
                    break;
            }
            if(userId==-1L){
                throw new RuntimeException("WrongEntity");
            }
        }catch(Exception e){
            log.info(e.getMessage());
        }
        return userId;
    }
}
