package com.DormitoryBack.domain.notification.service;

import com.DormitoryBack.domain.notification.dto.Notifiable;
//import com.DormitoryBack.domain.notification.dto.NotificationDto;
import com.DormitoryBack.domain.notification.entitiy.Notification;
import com.DormitoryBack.domain.notification.repository.NotificationRepository;
//import com.DormitoryBack.infrastructure.kafka.notification.KafkaProducerNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NotificationServiceExternal {
    @Autowired
    private NotificationRepository notificationRepository;

    //@Autowired
    //private KafkaProducerNotification producer;


    @Transactional
    public void saveAndPublishNotification(Notifiable subject, Notifiable trigger, String content){

        Notification notification=Notification.builder()
                .subjectType(subject.getEntityType())
                .subjectId(subject.getEntityId())
                .triggerType(trigger.getEntityType())
                .triggerId(trigger.getEntityId())
                .triggerContent(content)
                .triggeredDate(LocalDateTime.now())
                .isConfirmed(false)
                .build();

        notificationRepository.save(notification);
        
        /* 
        NotificationDto dto=NotificationDto.builder()
                .subject(subject)
                .trigger(trigger)
                .content(content)
                .build();
        */
        //producer.send(dto); <- send를 하지 않으면 실시간 알림 기능이 작동하지 않음.
        //실시간 알림 기능은 추후에 구현 예정
    }






}
