package com.DormitoryBack.domain.member.restriction.domain.service;

import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;
import com.DormitoryBack.domain.member.domain.service.UserServiceExternal;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionRequestDTO;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionResponseDTO;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionResponseDTOList;
import com.DormitoryBack.domain.member.restriction.domain.entity.Restriction;
import com.DormitoryBack.domain.member.restriction.domain.repository.RestrictionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RestrictionServiceTest {

    @InjectMocks
    private RestrictionService restrictionService;

    @Mock
    private RestrictionRepository restrictionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserServiceExternal userService;

    @Mock
    private TokenProvider tokenProvider;

    private final String validToken = "valid.token.here";
    
    private final Long userId = 1L;

    private final Long invalidUserId=-1L;

    private User user;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
            .id(userId)
            .build();


    }

    @Test
    public void testGetMyRestrictions() {
        Restriction restriction = new Restriction();
        restriction.setUserId(userId);
        restriction.setTriggeredTime(LocalDateTime.now());
        restriction.setDurationDays(5L);

        when(tokenProvider.getUserIdFromToken(validToken)).thenReturn(userId);
        when(restrictionRepository.findAllByUserId(userId)).thenReturn(Arrays.asList(restriction));

        RestrictionResponseDTOList result = restrictionService.getMyRestrictions(validToken);

        assertNotNull(result);
        assertEquals(1, result.getNumber());
    }

    @Test
    public void testRestrict() {
        RestrictionRequestDTO request=RestrictionRequestDTO.builder()
            .accessKey("??")
            .userId(userId)
            .reason("testRestriction")
            .durationDays(5L)
            .build();

        when(userService.isUserExist(userId)).thenReturn(true);
        when(restrictionRepository.save(any(Restriction.class))).thenAnswer(i -> i.getArguments()[0]);

        RestrictionResponseDTO result = restrictionService.restrict(request);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals("testRestriction", result.getReason());
    }

    @Test
    public void testWarn(){
        RestrictionRequestDTO request=RestrictionRequestDTO.builder()
            .accessKey("??")
            .userId(userId)
            .reason("testRestriction")
            .durationDays(null)
            .build();
        
        when(userService.isUserExist(userId)).thenReturn(true);
        when(restrictionRepository.save(any(Restriction.class))).thenAnswer(i -> i.getArguments()[0]);

        RestrictionResponseDTO response=restrictionService.warn(request);

        assertNotNull(response);
        assertEquals(userId, response.getUserId());
        assertNull(response.getExpireTime());
        assertNull(response.getIsExpired());
        assertEquals(response.getReason(), request.getReason());
    }

    @Test
    public void testWarn_RuntimeException_UserNotFound(){

        RestrictionRequestDTO request=RestrictionRequestDTO.builder()
        .accessKey("??")
        .userId(invalidUserId)
        .reason("testRestriction")
        .durationDays(null)
        .build();

        when(userService.isUserExist(invalidUserId)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            restrictionService.warn(request);
        });

        assertEquals("UserNotFound", exception.getMessage());

    }

    @Test
    public void testWarn_IllegalArgumentException_WarningCannotHaveDurationDays(){

        RestrictionRequestDTO request=RestrictionRequestDTO.builder()
        .accessKey("??")
        .userId(userId)
        .reason("testRestriction")
        .durationDays(1L)
        .build();

        when(userService.isUserExist(userId)).thenReturn(true);

        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class, ()->{
            restrictionService.warn(request);
        });

        assertEquals("WarningCannotHaveDurationDays", exception.getMessage());

    }

    @Test
    public void testGetIsRestricted_true() {
        LocalDateTime now=LocalDateTime.now();
        Restriction restriction1=Restriction.builder()
            .userId(userId)
            .triggeredTime(now.minusDays(2L))
            .durationDays(5L)
            .build();

        Restriction restriction2=Restriction.builder()
            .userId(userId)
            .triggeredTime(now.minusDays(8L))
            .durationDays(5L)
            .build();

        Restriction restriction3=Restriction.builder()
            .userId(userId)
            .triggeredTime(now)
            .durationDays(null)
            .build();

        when(restrictionRepository.findAllByUserId(userId)).thenReturn(Arrays.asList(restriction1,restriction2,restriction3));

        String result = (String)restrictionService.getIsRestricted(userId);

        LocalDateTime expiredTime=restrictionService.getExpiredTime(userId);
        String message="LoginRestricted:expiredAt:";
        String expected=message+expiredTime.toString();

        assertEquals(result, expected);
    
    }

    @Test
    public void testGetIsRestricted_false(){
        LocalDateTime now=LocalDateTime.now();
        Restriction restriction1=Restriction.builder()
            .userId(userId)
            .triggeredTime(now.minusDays(5L))
            .durationDays(5L)
            .build();

        Restriction restriction2=Restriction.builder()
            .userId(userId)
            .triggeredTime(now)
            .durationDays(null)
            .build();
        
        when(restrictionRepository.findAllByUserId(userId)).thenReturn(Arrays.asList(restriction1,restriction2));

        Object result=restrictionService.getIsRestricted(userId);

        assertFalse((Boolean)result);
    }

    @Test
    public void testGetExpiredTime() {
        LocalDateTime now=LocalDateTime.now();
        Restriction restriction1 = new Restriction();
        restriction1.setTriggeredTime(now.minusDays(1));
        restriction1.setDurationDays(3L);

        Restriction restriction2 = new Restriction();
        restriction2.setTriggeredTime(now.minusDays(2));
        restriction2.setDurationDays(5L);

        when(restrictionRepository.findAllByUserId(userId)).thenReturn(Arrays.asList(restriction1, restriction2));

        LocalDateTime result = restrictionService.getExpiredTime(userId);
        LocalDateTime expected=now.plusDays(3L);

        assertNotNull(result);
        assertEquals(expected, result);

    }

}
