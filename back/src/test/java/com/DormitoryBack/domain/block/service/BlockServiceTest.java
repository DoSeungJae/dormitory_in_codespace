package com.DormitoryBack.domain.block.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.DormitoryBack.domain.block.entity.Block;
import com.DormitoryBack.domain.block.repository.BlockRepository;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.service.UserServiceExternal;
import com.DormitoryBack.module.TimeOptimizer;

@ExtendWith(MockitoExtension.class)
public class BlockServiceTest {
    
    @InjectMocks
    private BlockService blockService;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private UserServiceExternal userService;

    @Mock
    private BlockRepository blockRepository;

    
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBlock_Success(){
        String token="validToken";
        Long blockedUserId=1L;
        Long blockerId=2L;

        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(token)).thenReturn(blockerId);
        when(userService.isUserExist(blockedUserId)).thenReturn(true);

        blockService.block(token, blockedUserId);

        verify(blockRepository,times(1)).save(any(Block.class));
    }

    @Test
    public void testGetBlockedIdList_Success(){
        String token="token";
        Long userId=1L;
        Block b1=Block.builder()
            .id(1L)
            .blockedUserId(2L)
            .blockerId(userId)
            .blockedTime(TimeOptimizer.now())
            .build();

        Block b2=Block.builder()
            .id(2L)
            .blockedUserId(3L)
            .blockerId(userId)
            .blockedTime(TimeOptimizer.now())
            .build();

        Block b3=Block.builder()
            .id(3L)
            .blockedUserId(4L)
            .blockerId(userId)
            .blockedTime(TimeOptimizer.now())
            .build();
        
        List<Block> blockedList=new ArrayList<>(List.of(b1,b2,b3)); 
        List<Long> blockedIdListExpected=new ArrayList<>(List.of(2L,3L,4L));

        when(tokenProvider.validateToken(token)).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(token)).thenReturn(userId);
        when(blockRepository.findAllByBlockerId(userId)).thenReturn(blockedList);

        List<Long> blockedIdList=blockService.getBlockedIdList(token);

        assertEquals(blockedIdListExpected.get(0), blockedIdList.get(0));
        assertEquals(blockedIdListExpected.get(1), blockedIdList.get(1));
        assertEquals(blockedIdListExpected.get(2), blockedIdList.get(2));
        assertEquals(blockedIdListExpected.size(), blockedIdList.size());
    }

}
