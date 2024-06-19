package com.DormitoryBack.domain.notification.service;

import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import com.DormitoryBack.domain.article.comment.domain.repository.CommentRepository;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.article.domain.repository.ArticleRepository;
import com.DormitoryBack.domain.group.domain.entitiy.Group;
import com.DormitoryBack.domain.group.domain.repository.GroupRepository;
import com.DormitoryBack.domain.member.entity.User;
import com.DormitoryBack.domain.member.repository.UserRepository;
import com.DormitoryBack.domain.notification.dto.Notifiable;
import com.DormitoryBack.domain.notification.dto.NotificationDto;
import com.DormitoryBack.domain.notification.entitiy.Notification;
import com.DormitoryBack.domain.notification.enums.EntityType;
import com.DormitoryBack.domain.notification.repository.NotificationRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    public List<NotificationDto> getAllNotifications() {
        List<Notification> notifications=notificationRepository.findAll();
        List<NotificationDto> dtoList=makeDtoList(notifications);
        return dtoList;
    }

    public List<NotificationDto> getAliveNotifications() {
        List<Notification> notifications=notificationRepository.findAliveNotifications();
        List<NotificationDto> dtoList=makeDtoList(notifications);
        return dtoList;
    }

    @Transactional
    public void confirmNotification(Long notificationId) {
        Notification notification=notificationRepository.findById(notificationId).orElse(null);
        if(notification==null){
            throw new RuntimeException("NotificationNotFound");
        }
        if(!checkExistenceOfSubjectAndTriggerWithLazyCascade(notification)){
            throw new RuntimeException("NotificationNotFound");
        }
        notification.setIsConfirmed(true);
        notificationRepository.save(notification);
    }
    public List<NotificationDto> makeDtoList(List<Notification> notifications){
        List<NotificationDto> dtoList=new ArrayList<>();
        Iterator<Notification> iterator=notifications.iterator();
        while(iterator.hasNext()){
            Notification notification=iterator.next();
            if(!checkExistenceOfSubjectAndTriggerWithLazyCascade(notification)){
                continue;
            }
            String subjectString=getStringifiedEntity(notification.getSubjectId(),notification.getSubjectType());
            String triggerString=getStringifiedEntity(notification.getTriggerId(),notification.getTriggerType());
            /*
            if(notification.getSubjectType()==EntityType.GROUP){
                if(!isGroupNotificationValid(notification.getTriggerType())){
                    continue;
                }
            }
             */

            Notifiable subject=Notifiable.builder()
                    .entityType(notification.getSubjectType())
                    .entityId(notification.getSubjectId())
                    .stringifiedEntity(subjectString)
                    .build();

            Notifiable trigger=Notifiable.builder()
                    .entityType(notification.getTriggerType())
                    .entityId(notification.getTriggerId())
                    .stringifiedEntity(triggerString)
                    .build();

            NotificationDto dto=NotificationDto.builder()
                    .subject(subject)
                    .trigger(trigger)
                    .content(notification.getTriggerContent())
                    .build();

            dtoList.add(dto);
        }
        return dtoList;
    }

    private Boolean checkExistenceOfSubjectAndTriggerWithLazyCascade(Notification notification){
        String subjectString=getStringifiedEntity(notification.getSubjectId(),notification.getSubjectType());
        String triggerString=getStringifiedEntity(notification.getTriggerId(),notification.getTriggerType());
        if(subjectString==null || triggerString==null){
            notificationRepository.delete(notification);
            return false;
        }
        return true;
    }

    /*
    private Boolean isGroupNotificationValid(EntityType triggerType){
        //group noti의 content부터 정의되어야 작성할 수 있음.
        switch (triggerType){
            case GROUP:
                break;
            case USER:
                break;
            case MESSAGE:
                break;
            default:
                break;

        }
    }

     */


    public String getStringifiedEntity(Long targetId, EntityType type){
        String entity;
        if(type==EntityType.ARTICLE){
            Article article=articleRepository.findById(targetId).orElse(null);
            entity=article.toJsonString();
        }
        else if(type==EntityType.COMMENT){
            Comment comment=commentRepository.findById(targetId).orElse(null);
            entity=comment.toJsonString();
        }
        else if(type==EntityType.GROUP){
            Group group=groupRepository.findById(targetId).orElse(null);
            entity=group.toJsonString();
        }
        else{//USER
            User user=userRepository.findById(targetId).orElse(null);
            entity=user.toJsonString();
        }

        return entity;
    }

}
