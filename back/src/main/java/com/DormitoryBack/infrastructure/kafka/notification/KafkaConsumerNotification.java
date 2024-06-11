package com.DormitoryBack.infrastructure.kafka.notification;


import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerNotification {
    @KafkaListener(topics = "notification", groupId = "group-delivery-box")
    public void consume(String message){
      log.info(message);
    }
}
