package com.DormitoryBack.domain.notification.module;

import com.DormitoryBack.domain.member.repository.UserRepository;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Getter
public class NotificationSocketManager {
    @Autowired
    private UserRepository userRepository;
    private HashMap<Long, SocketIOClient> socketClientsOfUsers=new HashMap<>();
    public void mapUserIdToSocketClient(Long userId,SocketIOClient client){
        socketClientsOfUsers.put(userId,client);
    }
    public void unmapUserIdToSocketClient(Long userId){
        if(userId==null){
            return ;
        }
        socketClientsOfUsers.remove(userId);
    }
    public SocketIOClient getSocketClientByUserId(Long userId){
        return socketClientsOfUsers.get(userId);
    }

}
