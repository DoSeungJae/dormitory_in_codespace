package com.DormitoryBack.domain.group.chat.domain.repository;

import com.DormitoryBack.domain.group.chat.domain.entity.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;

@EnableMongoRepositories
public interface MessageRepository extends MongoRepository<Message,String> {
    List<Message> findAllByRoom(String room);
}
