package com.DormitoryBack.domain.notification.service;

import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import com.DormitoryBack.domain.article.comment.domain.repository.CommentRepository;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.article.domain.repository.ArticleRepository;
import com.DormitoryBack.domain.group.domain.entitiy.Group;
import com.DormitoryBack.domain.group.domain.repository.GroupRepository;
import com.DormitoryBack.domain.member.entity.User;
import com.DormitoryBack.domain.member.repository.UserRepository;
import com.DormitoryBack.domain.notification.dto.NotificationDto;
import com.DormitoryBack.domain.notification.entitiy.Notification;
import com.DormitoryBack.domain.notification.enums.EntityType;
import com.DormitoryBack.domain.notification.repository.NotificationRepository;
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
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public List<NotificationDto> getAllNotifications() {
        List<Notification> notifications=notificationRepository.findAll();
        List<NotificationDto> dtoList=makeDtoList(notifications);
        return dtoList;
    }

    public List<NotificationDto> getAllUnconfirmedNotifications() {
        List<Notification> notifications=notificationRepository.findAllByIsConfirmed(false);
        List<NotificationDto> dtoList=makeDtoList(notifications);
        return dtoList;
    }

    @Transactional
    public void confirmNotification(Long notificationId) {
        Notification notification=notificationRepository.findById(notificationId).orElse(null);
        if(notification==null){
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
            EntityType sbjType=notification.getSubjectType();
            EntityType trgType=notification.getTriggerType();
            NotificationDto dto=NotificationDto.builder()
                    .subjectType(sbjType)
                    .subject(stringifyEntity(notification.getSubjectId(),sbjType))
                    .triggerType(trgType)
                    .trigger(stringifyEntity(notification.getTriggerId(),trgType))
                    .build();

            dtoList.add(dto);
        }
        return dtoList;
    }

    public String stringifyEntity(Long targetId, EntityType type){
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
