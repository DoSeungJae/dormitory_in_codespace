package com.DormitoryBack.domain.notification.api;

import com.DormitoryBack.domain.notification.dto.NotificationDto;
import com.DormitoryBack.domain.notification.entitiy.Notification;
import com.DormitoryBack.domain.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/notification")
@Slf4j
public class NotificationApi {
    private final NotificationService notificationService;
    @Autowired
    public NotificationApi(NotificationService notificationService){
        this.notificationService=notificationService;
    }
    @GetMapping("")
    public ResponseEntity allNotifications(){
        List<NotificationDto> notifications=notificationService.getAllNotifications();
        return ResponseEntity.status(HttpStatus.OK).body(notifications);
    }
    @GetMapping("/alive")
    public ResponseEntity allUnconfirmedNotifications(){
        List<NotificationDto> notifications=notificationService.getAliveNotifications();
        return ResponseEntity.status(HttpStatus.OK).body(notifications);
    }
    @GetMapping("/myNotifications")
    public ResponseEntity myNotifications(@RequestHeader("Authorization") String token){
        List<NotificationDto> myNotifications=notificationService.getMyNotifications(token);
        return ResponseEntity.status(HttpStatus.OK).body(myNotifications);
    }
    @PatchMapping("/confirm/{notificationId}")
    public ResponseEntity confirm(@PathVariable("notificationId") Long notificationId){
        notificationService.confirmNotification(notificationId);
        return ResponseEntity.status(HttpStatus.OK).body("NotificationConfirmed");
    }




}
