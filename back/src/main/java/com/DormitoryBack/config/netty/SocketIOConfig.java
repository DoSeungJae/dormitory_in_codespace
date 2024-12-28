package com.DormitoryBack.config.netty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corundumstudio.socketio.SocketIOServer;

@Configuration
public class SocketIOConfig { //SocketIOConfigChat 으로 변경 필요

    @Value("${socketServer.host}")
    private String host;

    @Value("${socketServer.port}")
    private Integer port;

    @Value("${socketServer.url}")
    private String url;

    @Bean
    public SocketIOServer socketIOServer() { //socketIOServerChat 으로 변경 필요

        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setOrigin(url);

        return new SocketIOServer(config);
    }

}