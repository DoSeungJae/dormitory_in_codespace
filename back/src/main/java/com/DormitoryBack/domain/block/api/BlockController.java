package com.DormitoryBack.domain.block.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DormitoryBack.domain.block.service.BlockService;

@CrossOrigin(origins = {"https://improved-space-tribble-vjvwrwx956jh69w4-3000.app.github.dev", "https://turbo-chainsaw-rpvvwx9pp5c5p55-3000.app.github.dev"})
@RestController
@RequestMapping("api/v1/block")
public class BlockController {
    
    @Autowired
    private BlockService blockService;

    @PostMapping("/{targetUserId}")
    public ResponseEntity<Void> blockUser(@RequestHeader("Authorization") String token, @PathVariable("targetUserId") Long blockedUserId){
        blockService.block(token,blockedUserId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

    }
    
}
