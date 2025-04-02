package com.DormitoryBack.domain.block.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.DormitoryBack.domain.block.entity.Block;
import com.DormitoryBack.domain.block.repository.BlockRepository;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.service.UserServiceExternal;
import com.DormitoryBack.module.TimeOptimizer;

@Service
public class BlockService {
    
    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserServiceExternal userService;

    public void block(String token, Long blockedUserId){
        if(!tokenProvider.validateToken(token)){
            throw new RuntimeException("InvalidToken");
        }
        Long blockerId=tokenProvider.getUserIdFromToken(token);
        if(!userService.isUserExist(blockedUserId)){
            throw new RuntimeException("UserNotFound"); //userService(External)에서 예외처리 하는 것이 더 좋을 듯...
        }

        Block block=Block.builder()
            .blockedUserId(blockedUserId)
            .blockerId(blockerId)
            .blockedTime(TimeOptimizer.now())
            .build();

        blockRepository.save(block);
    }

    public List<Long> getBlockedIdList(String token){
        if(!tokenProvider.validateToken(token)){
            throw new RuntimeException("InvalidToken");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        List<Block> blockList=blockRepository.findAllByBlockerId(userId);
        List<Long> blockedIdList=new ArrayList<>();
        for(Block block : blockList){
            Long blockedId=block.getBlockedUserId();
            blockedIdList.add(blockedId);
        }
        return blockedIdList;
    }


}
