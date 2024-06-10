package com.DormitoryBack.config.netty;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIOConfigKafka {

    @Value("${socketServerKafka.host}")
    private String host;

    @Value("${socketServerKafka.port}")
    private Integer port;

    @Bean
    public SocketIOServer socketIOServerKafka(){
        com.corundumstudio.socketio.Configuration kafkaConfig=new com.corundumstudio.socketio.Configuration();
        kafkaConfig.setHostname(host);
        kafkaConfig.setPort(port);
        kafkaConfig.setOrigin("http://localhost:3000");

        return new SocketIOServer(kafkaConfig);
    }

}
