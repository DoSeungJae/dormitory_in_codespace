package com.DormitoryBack.infrastructure.kafka.notification;

import com.DormitoryBack.domain.notification.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerNotification {
    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;
    private static final String topic="notification";
    public void send(NotificationDto dto){
        kafkaTemplate.send(topic,dto.toJsonString());
    }
}
