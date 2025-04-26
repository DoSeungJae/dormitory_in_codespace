package com.DormitoryBack.domain.block.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import com.DormitoryBack.domain.block.exception.UnblockableException;
import com.DormitoryBack.domain.block.repository.BlockRepository;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.service.UserServiceExternal;
import com.DormitoryBack.exception.ErrorType;
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
        Block block=Block.builder()
            .id(1L)
            .blockedUserId(blockedUserId)
            .blockerId(blockerId)
            .blockedTime(TimeOptimizer.now())
            .build();

        when(userService.isUserExist(blockedUserId)).thenReturn(true);
        assertDoesNotThrow(()->tokenProvider.validateTokenOrThrow(token));
        when(tokenProvider.getUserIdFromToken(token)).thenReturn(blockerId);
        assertDoesNotThrow(()->blockService.checkBlockableOrThrow(blockerId, blockedUserId));
        
        blockService.block(token, blockedUserId);

        verify(blockRepository,times(1)).save(any(Block.class));
        assertEquals(blockerId, block.getBlockerId());
        assertEquals(blockedUserId, block.getBlockedUserId());
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

        assertDoesNotThrow(()->tokenProvider.validateTokenOrThrow(token));
        when(tokenProvider.getUserIdFromToken(token)).thenReturn(userId);
        when(blockRepository.findAllByBlockerId(userId)).thenReturn(blockedList);

        List<Long> blockedIdList=blockService.getBlockedIdList(token);

        assertEquals(blockedIdListExpected.get(0), blockedIdList.get(0));
        assertEquals(blockedIdListExpected.get(1), blockedIdList.get(1));
        assertEquals(blockedIdListExpected.get(2), blockedIdList.get(2));
        assertEquals(blockedIdListExpected.size(), blockedIdList.size());
    }


    @Test
    public void testCheckBlockableOrThrow_Success(){
        Long blockerId=1L;
        Long blockedUserId=2L;

        when(userService.isUserExist(blockedUserId)).thenReturn(true);
        //when(userService.isDeletedUserExist(blockedUserId)).thenReturn(true);

        assertDoesNotThrow(()->blockService.checkBlockableOrThrow(blockerId, blockedUserId));
    }

    @Test
    public void testCheckBlockableOrThrow_Success2(){
        Long blockerId=1L;
        Long blockedUserId=2L;

        when(userService.isUserExist(blockedUserId)).thenReturn(false);
        when(userService.isDeletedUserExist(blockedUserId)).thenReturn(true);

        assertDoesNotThrow(()->blockService.checkBlockableOrThrow(blockerId, blockedUserId));
    }

    @Test
    public void testCheckBlockableOrThrow_UnblockableException_BlockSelf(){
        Long blockerId=1L;
        Long blockedUserId=1L;

        UnblockableException exception=assertThrows(UnblockableException.class, ()->{
            blockService.checkBlockableOrThrow(blockerId, blockedUserId);
        });

        assertEquals("자기 자신을 차단할 수 없습니다.", exception.getErrorInfo().getErrorMessage());
        assertEquals(ErrorType.BlockSelf, exception.getErrorInfo().getErrorType());
    }

    @Test
    public void testCheckBlockableOrThorw_UnblockableException_EntityNotFound(){
        Long blockerId=1L;
        Long blockedUserId=2L;

        when(userService.isUserExist(blockedUserId)).thenReturn(false);
        when(userService.isDeletedUserExist(blockedUserId)).thenReturn(false);

        UnblockableException exception=assertThrows(UnblockableException.class, ()->{
            blockService.checkBlockableOrThrow(blockerId, blockedUserId);
        });

        assertEquals("차단할 수 없는 사용자입니다.", exception.getErrorInfo().getErrorMessage());
        assertEquals(ErrorType.EntityNotFound, exception.getErrorInfo().getErrorType());
    }



}
