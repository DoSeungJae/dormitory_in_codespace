package com.DormitoryBack.config.netty;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOServer;

@Component
public class ServerCommandLineRunner implements CommandLineRunner {

    private final SocketIOServer chatServer;
    //private final SocketIOServer kafkaServer;

    public ServerCommandLineRunner(
            @Qualifier("socketIOServer") SocketIOServer chatServer){
            // ,@Qualifier("socketIOServerKafka") SocketIOServer kafkaServer){

        this.chatServer=chatServer;
        //this.kafkaServer=kafkaServer;
    }

    @Override
    public void run(String... args) throws Exception {
        chatServer.start();
        //kafkaServer.start();
    }
}