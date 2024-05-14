package com.DormitoryBack.domain.group.chat.domain.repository;

import com.DormitoryBack.domain.group.chat.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {
    List<Message> findAllByRoom(String room);
    //Page,Pageable로 변경 고려

}
