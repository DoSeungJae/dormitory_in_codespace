package com.DormitoryBack.domain.notification.service;

import com.DormitoryBack.domain.notification.module.NotificationSocketManager;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class NotificationSocketService {
    private final NotificationSocketManager socketManager;
    public void sendNotification(String message, Set<Long> targetsOfIds){

        /*
        SocketIOClient socketClient=socketManager.getSocketClientByUserId(targetUserId);
        if(socketClient==null){
            return ;
        }
        socketClient.sendEvent("notification",message);

         */
    }
}
