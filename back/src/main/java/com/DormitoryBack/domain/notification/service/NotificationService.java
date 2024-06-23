package com.DormitoryBack.domain.notification.service;

import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import com.DormitoryBack.domain.article.comment.domain.repository.CommentRepository;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.article.domain.repository.ArticleRepository;
import com.DormitoryBack.domain.group.domain.entitiy.Group;
import com.DormitoryBack.domain.group.domain.repository.GroupRepository;
import com.DormitoryBack.domain.group.domain.service.GroupServiceExternal;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.entity.User;
import com.DormitoryBack.domain.member.repository.UserRepository;
import com.DormitoryBack.domain.notification.constant.NotificationConstants;
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
    private ObjectMapper objectMapper=new ObjectMapper();
    private GroupServiceExternal groupService;
    private TokenProvider tokenProvider;

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

    public List<NotificationDto> getMyNotifications(String token) {
        Long userId=tokenProvider.getUserIdFromToken(token);
        List<Notification> notifications=notificationRepository.findAliveNotifications();
        List<NotificationDto> myDtoList=new ArrayList<>();
        Iterator<Notification> iterator=notifications.iterator();
        while(iterator.hasNext()){
            Notification notification= iterator.next();
            Long subjectUserId=getEntityUserId(notification.getSubjectId(),notification.getSubjectType());
            Long triggerUserId=getEntityUserId(notification.getTriggerId(),notification.getTriggerType());
            if(notification.getSubjectType()==EntityType.GROUP){
                Long groupId;
                try{
                    groupId=objectMapper
                            .readTree(getStringifiedEntity(notification.getSubjectId(),EntityType.GROUP))
                            .get("id")
                            .asLong();
                }catch(Exception e){
                    throw new RuntimeException(e.getMessage());
                }

                if(groupService.isMember(groupId,userId) && userId!=triggerUserId){
                    String subjectString=getStringifiedEntity(notification.getSubjectId(),notification.getSubjectType());
                    String triggerString=getStringifiedEntity(notification.getTriggerId(),notification.getTriggerType());
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

                    myDtoList.add(dto);
                }
            }
            else{
                if(userId==subjectUserId && userId!=triggerUserId){
                    String subjectString=getStringifiedEntity(notification.getSubjectId(),notification.getSubjectType());
                    String triggerString=getStringifiedEntity(notification.getTriggerId(),notification.getTriggerType());
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

                    myDtoList.add(dto);
                }
            }
        }

        return myDtoList;
    }
    private List<NotificationDto> makeDtoList(List<Notification> notifications){
        List<NotificationDto> dtoList=new ArrayList<>();
        Iterator<Notification> iterator=notifications.iterator();
        while(iterator.hasNext()){
            Notification notification=iterator.next();
            if(!checkExistenceOfSubjectAndTriggerWithLazyCascade(notification)){
                continue;
            }

            if(notification.getSubjectType()==EntityType.GROUP){
                if(!isGroupNotificationValid(notification)){
                    notificationRepository.delete(notification);
                    continue;
                }
            }

            String subjectString=getStringifiedEntity(notification.getSubjectId(),notification.getSubjectType());
            String triggerString=getStringifiedEntity(notification.getTriggerId(),notification.getTriggerType());
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

    @Transactional
    private Boolean checkExistenceOfSubjectAndTriggerWithLazyCascade(Notification notification){
        String subjectString=getStringifiedEntity(notification.getSubjectId(),notification.getSubjectType());
        String triggerString=getStringifiedEntity(notification.getTriggerId(),notification.getTriggerType());
        if(subjectString==null || triggerString==null){
            notificationRepository.delete(notification);
            return false;
        }
        return true;
    }

    private Boolean isGroupNotificationValid(Notification notification){
        //group noti의 content부터 정의되어야 작성할 수 있음.
        EntityType triggerType=notification.getTriggerType();
        String triggerContent=notification.getTriggerContent();

        switch (triggerType){
            case GROUP:
                if(NotificationConstants.getConstantName(triggerContent)==NotificationConstants.GROUP_FINISHED_KOR){ //-2L
                    //현재 그룹의 상태가 종료된 상태라면 유효한 알림
                    if(groupService.getGroupState(notification.getSubjectId())==-2L){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                else if(NotificationConstants.getConstantName(triggerContent)==NotificationConstants.GROUP_CLOSED_KOR){ //-1L
                    //현재 그룹의 상태가 마감된 상태라면 유효한 알림
                    if(groupService.getGroupState(notification.getSubjectId())==-1L){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
                break;
            case USER:
                //trigger id == user id
                if(NotificationConstants.getConstantName(triggerContent)==NotificationConstants.MEMBER_EXPELLED_KOR){
                    //triggerUserId가 해당 group에 속하지 않았다면 유효한 알림
                    return !groupService.isMember(notification.getSubjectId(),notification.getTriggerId());
                }
                else if(NotificationConstants.getConstantName(triggerContent)==NotificationConstants.MEMBER_ENTER_GROUP_KOR){
                    //triggerUserId가 해당 group에 속한 상태라면 유효한 알림
                    return groupService.isMember(notification.getSubjectId(),notification.getTriggerId());
                }
                else if(NotificationConstants.getConstantName(triggerContent)==NotificationConstants.MEMBER_LEFT_KOR){
                    //triggerUserId가 해당 group에 속하지 않았다면 유효한 알림
                    return !groupService.isMember(notification.getSubjectId(),notification.getTriggerId());
                }
                break;
            default:
                break;

        }
        return false;
    }


    private String getStringifiedEntity(Long targetId, EntityType type){
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

    private Long getEntityUserId(Long entityId,EntityType type){
        Long userId;
        if(type==EntityType.ARTICLE){
            Article article=articleRepository.findById(entityId).orElse(null);
            userId=article.getUserId();
        }
        else if(type==EntityType.COMMENT){
            Comment comment=commentRepository.findById(entityId).orElse(null);
            userId=comment.getUser().getId();
        }
        else if(type==EntityType.GROUP){
            Group group=groupRepository.findById(entityId).orElse(null);
            userId=group.getHostId();
        }
        else{//USER
            User user=userRepository.findById(entityId).orElse(null);
            userId=user.getId();
        }
        return userId;
    }



}
