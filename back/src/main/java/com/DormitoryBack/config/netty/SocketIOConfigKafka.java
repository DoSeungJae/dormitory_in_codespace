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
        kafkaConfig.setOrigin("https://improved-space-tribble-vjvwrwx956jh69w4-3000.app.github.dev/");

        return new SocketIOServer(kafkaConfig);
    }

}
