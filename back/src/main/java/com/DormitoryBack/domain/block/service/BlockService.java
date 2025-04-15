package com.DormitoryBack.domain.block.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.DormitoryBack.domain.block.entity.Block;
import com.DormitoryBack.domain.block.exception.UnblockableException;
import com.DormitoryBack.domain.block.repository.BlockRepository;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.service.UserServiceExternal;
import com.DormitoryBack.exception.ErrorInfo;
import com.DormitoryBack.exception.ErrorType;
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
        tokenProvider.validateTokenOrThrow(token);
        Long blockerId=tokenProvider.getUserIdFromToken(token);
        checkBlockableOrThrow(blockerId, blockedUserId);

        Block block=Block.builder()
            .blockedUserId(blockedUserId)
            .blockerId(blockerId)
            .blockedTime(TimeOptimizer.now())
            .build();

        blockRepository.save(block);
    }

    public void checkBlockableOrThrow(Long blockerId, Long blockedUserId){
        if(blockerId==blockedUserId){
            throw new UnblockableException(new ErrorInfo(ErrorType.BlockSelf, "자기 자신을 차단할 수 없습니다."));
        }
        if(!userService.isUserExist(blockedUserId) && !userService.isDeletedUserExist(blockedUserId)){
            throw new UnblockableException(new ErrorInfo(ErrorType.EntityNotFound, "차단할 수 없는 사용자입니다."));
        }
    }
    

    public List<Long> getBlockedIdList(String token){
        tokenProvider.validateTokenOrThrow(token);
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
