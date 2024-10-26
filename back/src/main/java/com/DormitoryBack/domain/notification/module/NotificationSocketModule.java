package com.DormitoryBack.domain.notification.module;

/*
import com.DormitoryBack.domain.notification.service.NotificationSocketService;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationSocketModule {
    private final SocketIOServer server;
    private final NotificationSocketService socketService;
    private final NotificationSocketManager socketManager;
    public NotificationSocketModule(@Qualifier("socketIOServerKafka") SocketIOServer server, NotificationSocketService socketService, NotificationSocketManager socketManager){
        this.server=server;
        this.socketService=socketService;
        this.socketManager=socketManager;
        server.addConnectListener(this.onConnected());
        server.addDisconnectListener(this.onDisconnected());
    }
    private ConnectListener onConnected(){
        return (client) -> {
            var params = client.getHandshakeData().getUrlParams();

            List<String> userIdList = params.get("userId");
            String userId=(!userIdList.isEmpty()) ? userIdList.get(0) : "";
            if(userIdList.isEmpty()){
                return ;
            }
            socketManager.mapUserIdToSocketClient(Long.valueOf(userId),client);
        };
    }
    private DisconnectListener onDisconnected(){
        return (client)->{
            var params=client.getHandshakeData().getUrlParams();
            List<String> userIdList=params.get("userId");
            String userId=(!userIdList.isEmpty())?userIdList.get(0):"";
            if(userIdList.isEmpty()){
                return ;
            }
            socketManager.unmapUserIdToSocketClient(Long.valueOf(userId));
        };
    }

}
 */