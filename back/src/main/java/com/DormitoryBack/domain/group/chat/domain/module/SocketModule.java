package com.DormitoryBack.domain.group.chat.domain.module;

import com.DormitoryBack.domain.group.chat.domain.constant.Constants;
import com.DormitoryBack.domain.group.chat.domain.entity.Message;
import com.DormitoryBack.domain.group.chat.domain.service.SocketService;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SocketModule {
    private final SocketIOServer server;
    private final SocketService socketService;
    public SocketModule(SocketIOServer server, SocketService socketService) {
        this.server = server;
        this.socketService = socketService;
        server.addConnectListener(this.onConnected());
        server.addDisconnectListener(this.onDisconnected());
        server.addEventListener("sendMessage", Message.class, this.onChatReceived());
    }

    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.saveMessage(senderClient, data);
        };
    }

    
    private ConnectListener onConnected() {
        return (client) -> {
            var params = client.getHandshakeData().getUrlParams();

            // room과 username 값이 null인지 체크하고, null이면 빈 문자열을 사용
            List<String> roomList = params.get("room");
            String room=(!roomList.isEmpty()) ? roomList.get(0) : "";
            if(roomList.isEmpty()){
                return ;
            }
            List<String> usernameList = params.get("username");
            String username=(!usernameList.isEmpty()) ? usernameList.get(0) : "";
            if(usernameList.isEmpty()){
                return ;
            }
            socketService.saveInfoMessage(client, String.format(Constants.WELCOME_MESSAGE, username), room);
            log.info("Socket ID[{}] - room[{}] - username [{}]  Connected to chat module through", client.getSessionId().toString(), room, username);
            log.info(usernameList.toString());
        };
    }


    private DisconnectListener onDisconnected() {
        return client -> {
            var params = client.getHandshakeData().getUrlParams();
            List<String> roomList = params.get("room");
            String room=(!roomList.isEmpty()) ? roomList.get(0) : "";
            if(roomList.isEmpty()){
                return ;
            }
            List<String> usernameList = params.get("username");
            String username=(!usernameList.isEmpty()) ? usernameList.get(0) : "";
            if(usernameList.isEmpty()){
                return ;
            }
            socketService.saveInfoMessage(client, String.format(Constants.DISCONNECT_MESSAGE, username), room);
            log.info("Socket ID[{}] - room[{}] - username [{}]  disconnected to chat module through", client.getSessionId().toString(), room, username);
        };
    }

}