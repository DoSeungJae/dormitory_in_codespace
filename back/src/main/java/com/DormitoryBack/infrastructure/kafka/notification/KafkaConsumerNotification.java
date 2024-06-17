package com.DormitoryBack.infrastructure.kafka.notification;


import com.DormitoryBack.domain.member.repository.UserRepository;
import com.DormitoryBack.domain.notification.service.NotificationSocketService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerNotification {
    private final NotificationSocketService notificationSocketService;
    private final ObjectMapper objectMapper= new ObjectMapper();
    private final UserRepository userRepository;
    @KafkaListener(topics = "notification", groupId = "group-delivery-box")
    public void consume(String message){
      log.info(message);
      HashMap<String,Long> ids=getIdsOfTargets(message);
      Long subjectUserId=ids.get("subject");
      Long triggerUserId=ids.get("trigger");
      if(subjectUserId==null || triggerUserId==null){
          return ;
      }
      //Set<Long> targetIds=getTargetIds()
      //notificationSocketService.sendNotification(message,);

    }

    public Boolean judgeValidity(){
        return true;

    }

    public HashMap<String,Long> getIdsOfTargets(String message){
        HashMap<String,Long> IdsOfTargets=new HashMap<>();
        try{
            JsonNode messageNode=objectMapper.readTree(message);
            JsonNode subject=messageNode.get("subject");
            JsonNode trigger=messageNode.get("trigger");
            Long subjectUserId=getUserIdFromEntity(subject);
            Long triggerUserId=getUserIdFromEntity(trigger);
            IdsOfTargets.put("subject",subjectUserId);
            IdsOfTargets.put("trigger",triggerUserId);

        }catch(JsonProcessingException e){
            throw new RuntimeException(e.getMessage());
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
        return IdsOfTargets;
    }

    public Long getUserIdFromEntity(JsonNode entityNode){
        String entityType=entityNode.get("entityType").asText();
        JsonNode entity=entityNode.get("stringifiedEntity");
        Long userId;
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
            case "MESSAGE":
                String userName=entity.get("username").asText();
                userId=userRepository.findByNickName(userName).getId();
                break;
            default:
                userId=null;
                break;
        }
        if(userId==null){
            throw new RuntimeException("WrongEntity");
        }
        return userId;

    }
}
