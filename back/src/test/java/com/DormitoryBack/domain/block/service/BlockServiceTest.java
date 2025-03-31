package com.DormitoryBack.domain.block.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.DormitoryBack.domain.block.entity.Block;
import com.DormitoryBack.domain.block.entity.Blockable;
import com.DormitoryBack.domain.block.repository.BlockRepository;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.service.UserServiceExternal;

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

    @Mock
    private Blockable blockable;
    
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
    public void testIsBlocked_false(){
        Long blockedUserId=1L;
        Long blockerId=2L;

        when(blockable.getUserId()).thenReturn(blockedUserId);
        when(blockRepository.countByBlockedUserIdAndBlockerId(blockedUserId, blockerId)).thenReturn(0L);

        Boolean isBlocked=blockService.isBlocked(blockable, blockerId);

        assertEquals(false, isBlocked);

    }

    @Test
    public void testIsBlocked_true(){
        Long blockedUserId=1L;
        Long blockerId=2L;

        when(blockable.getUserId()).thenReturn(blockedUserId);
        when(blockRepository.countByBlockedUserIdAndBlockerId(blockedUserId, blockerId)).thenReturn(3L);

        Boolean isBlocked=blockService.isBlocked(blockable, blockerId);

        assertEquals(true, isBlocked);

    }


}
