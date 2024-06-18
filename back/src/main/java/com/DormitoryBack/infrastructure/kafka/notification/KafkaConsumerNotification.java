package com.DormitoryBack.infrastructure.kafka.notification;

import com.DormitoryBack.domain.notification.service.NotificationSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerNotification {
    private final NotificationSocketService notificationSocketService;
    @KafkaListener(topics = "notification", groupId = "group-delivery-box")
    public void consume(String message){
      notificationSocketService.sendNotification(message);
    }
}
