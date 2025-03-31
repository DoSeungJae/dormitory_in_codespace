package com.DormitoryBack.domain.group.chat.domain.module;

import com.DormitoryBack.domain.member.domain.dto.UserResponseDTO;
import com.DormitoryBack.domain.member.domain.service.UserServiceExternal;
import com.corundumstudio.socketio.SocketIOClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;

@Slf4j
@Component
@Getter
public class ChatManager {

    @Autowired
    private UserServiceExternal userService;

    private HashMap<Long, SocketIOClient> socketClientsOfUsers=new HashMap<>(); //부적절한 방식임. 여러 대의 서버로 운용할 경우에 문제가 생김. db에서 관리해야함... 
    
    public void mapUserIdToSocketClient(String username, SocketIOClient client) {
        UserResponseDTO user=userService.getUserByNickName(username);
        Long userId=user.getId();
        socketClientsOfUsers.put(userId,client);
        //동시성 이슈 발생 가능성 고려해야함(아마 24.04?) <- 어떤 의미로 적어놓은건지 기억이 안난다....(25.03.30)
    }
    public SocketIOClient getSocketClientByUserId(Long userId){
        SocketIOClient client=socketClientsOfUsers.get(userId);
        if(client==null){
            return null;
        }
        return socketClientsOfUsers.get(userId);
    }
    public void removeSocketClientByUserId(String username){
        UserResponseDTO user=userService.getUserByNickName(username);
        Long userId=user.getId();
        socketClientsOfUsers.remove(userId);
    }

}