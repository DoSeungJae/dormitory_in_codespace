package com.DormitoryBack.domain.group.chat.domain.api;

import com.DormitoryBack.domain.group.chat.domain.entity.Message;
import com.DormitoryBack.domain.group.chat.domain.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageApi {

    private final MessageService messageService;

    @GetMapping("/test")
    public String chatTest(){
        return "test in chat test";
    }
    @GetMapping("/{room}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String room) {
        return ResponseEntity.ok(messageService.getMessages(room));
    }

}
