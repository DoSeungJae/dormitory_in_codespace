package com.DormitoryBack.domain.group.chat.domain.module;

import com.DormitoryBack.domain.group.domain.entitiy.Group;
import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@Getter
public class ChatManager {

    @Autowired
    private UserRepository userRepository;
    private HashMap<Long, SocketIOClient> socketClientsOfUsers=new HashMap<>();
    public void mapUserIdToSocketClient(String username,SocketIOClient client) {
        User user=userRepository.findByNickName(username);
        Long userId=user.getId();
        socketClientsOfUsers.put(userId,client);
        //동시성 이슈 발생 가능성 고려해야함
    }
    public SocketIOClient getSocketClientByUserId(Long userId){
        SocketIOClient client=socketClientsOfUsers.get(userId);
        if(client==null){
            return null;
        }
        return socketClientsOfUsers.get(userId);
    }
    public void removeSocketClientByUserId(String username){
        User user=userRepository.findByNickName(username);
        if(user==null){
            return ;
        }
        Long userId=user.getId();
        socketClientsOfUsers.remove(userId);
    }

}
