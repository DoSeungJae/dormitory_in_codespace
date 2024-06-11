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
import com.DormitoryBack.infrastructure.kafka.notification.KafkaProducerNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NotificationServiceExternal {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KafkaProducerNotification producer;


    @Transactional
    public void saveAndPublishNotification(EntityType subjectType, Long subjectId, EntityType triggerType, Long triggerId,String content){

        Notification notification=Notification.builder()
                .subjectType(subjectType)
                .subjectId(subjectId)
                .triggerType(triggerType)
                .triggerId(triggerId)
                .triggerContent(content)
                .triggeredDate(LocalDateTime.now())
                .isConfirmed(false)
                .build();

        notificationRepository.save(notification);
        NotificationDto dto=NotificationDto.builder()
                .subjectType(subjectType)
                .subject(stringifyEntity(subjectId,subjectType))
                .triggerType(triggerType)
                .trigger(stringifyEntity(triggerId,triggerType))
                .build();

        producer.send(dto);
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
