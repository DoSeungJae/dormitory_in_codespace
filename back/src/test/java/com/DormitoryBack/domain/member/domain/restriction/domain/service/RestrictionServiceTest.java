package com.DormitoryBack.domain.member.domain.restriction.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionRequestDTO;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionResponseDTO;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionResponseDTOList;
import com.DormitoryBack.domain.member.restriction.domain.entity.Restriction;
import com.DormitoryBack.domain.member.restriction.domain.enums.Function;
import com.DormitoryBack.domain.member.restriction.domain.repository.RestrictionRepository;
import com.DormitoryBack.domain.member.restriction.domain.service.RestrictionService;
import com.DormitoryBack.module.TimeOptimizer;
import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class RestrictionServiceTest {
    
    @Mock
    private TimeOptimizer timeOptimizer;

    @Mock
    private RestrictionRepository restrictionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RestrictionService restrictionService;

    @Captor
    private ArgumentCaptor<Restriction> restirctionCaptor;

    private User user;

    private Restriction restriction;

    private Restriction restriction1;

    private Restriction restriction2;

    private Restriction restriction3;


    private Restriction r1;

    private Restriction r2;

    private Restriction r3;

    private Restriction r4;

    
    private RestrictionResponseDTOList expectedResponseDTOList;

    private RestrictionRequestDTO requestDTO;

    private List<RestrictionResponseDTO> expectedDTOList;

    @Value("administrator.key")
    private String key;

    @BeforeEach
    public void setUp(){
        restriction= Restriction.builder()
            .userId(1L)
            .reason("test reason")
            .durationDays(10L)
            .triggeredTime(LocalDateTime.of(2024,12,21,0,0))
            .suspendedFunctions(0) //정책 상 suspendedFunctions는 0이 될 수 없음. 
            //해당 데이터 필드가 0이라는 것은 제한된 기능이 없다는 의미이므로 restriction 인스턴스 존재 자체가 모순임
            .build();

        List<String> suspendedFunctions=Arrays.asList("LOGIN","ARTICLE");
        requestDTO=RestrictionRequestDTO.builder()
            .accessKey("20220393-2470011192")
            .userId(1L)
            .reason("test reason")
            .durationDays(10L)
            .suspendedFunctions(suspendedFunctions)
            .build();

        restriction1 = Restriction.builder()
            .userId(1L)
            .reason("reason1")
            .durationDays(1L)
            .triggeredTime(LocalDateTime.of(2024,12,21,0,0))
            .suspendedFunctions(1)
            .build();

        restriction2=Restriction.builder()
            .userId(2L)
            .reason("reason2")
            .durationDays(2L)
            .triggeredTime(LocalDateTime.of(2024,12,21,0,0))
            .suspendedFunctions(3)
            .build();
        
        restriction3=Restriction.builder()
            .userId(3L)
            .reason("reason3")
            .durationDays(3L)
            .triggeredTime(LocalDateTime.of(2024,12,21,0,0))
            .suspendedFunctions(7)
            .build();


        expectedDTOList=new ArrayList<>();
        expectedDTOList.add(restrictionService.makeDTO(restriction1));
        expectedDTOList.add(restrictionService.makeDTO(restriction2));
        expectedDTOList.add(restrictionService.makeDTO(restriction3));

        expectedResponseDTOList=RestrictionResponseDTOList.builder()
            .dtoList(expectedDTOList)
            .number(expectedDTOList.size())
            .build();   
    }

    @Test
    public void testMakeDTO(){
        LocalDateTime now= LocalDateTime.of(2024,12,31,0,0,0);
        try (var mockedTimeOptimizer = mockStatic(TimeOptimizer.class)){
            mockedTimeOptimizer.when(TimeOptimizer::now).thenReturn(now);

            RestrictionResponseDTO dto=restrictionService.makeDTO(restriction);
    
            assertEquals(restriction.getUserId(), dto.getUserId());
            assertEquals(restriction.getTriggeredTime().plusDays(restriction.getDurationDays()),dto.getExpireTime());
            assertTrue(dto.getIsExpired());
            assertEquals(restriction.getReason(), dto.getReason());
        }    
    }

    @Test
    public void testRestrict(){
        LocalDateTime now=LocalDateTime.of(2024,12,21,0,0);
        user=User.builder()
            .id(1L)
            //.eMail("email")
            .passWord("passWord")
            .nickName("nickName")
            .dormId(1L)
            .build();

        try (var mockedTimeOptimizer = mockStatic(TimeOptimizer.class)){
            mockedTimeOptimizer.when(TimeOptimizer::now).thenReturn(now);

            given(restrictionRepository.save(restirctionCaptor.capture())).willReturn(restriction);
            given(userRepository.findById(1L)).willReturn(Optional.of(user));
            RestrictionResponseDTO responseDTO=restrictionService.restrict(requestDTO);
            
            verify(restrictionRepository,times(1)).save(restirctionCaptor.capture());
            Restriction capturedRestriction=restirctionCaptor.getValue();
            assertEquals(requestDTO.getUserId(),capturedRestriction.getUserId());
            assertEquals(requestDTO.getReason(),capturedRestriction.getReason());
            assertEquals(requestDTO.getDurationDays(),capturedRestriction.getDurationDays());
            assertEquals(now, capturedRestriction.getTriggeredTime());
            assertEquals(3, capturedRestriction.getSuspendedFunctions());
        }
    }

    @Test
    public void testRestrictWithInvalidKey(){
        requestDTO.setAccessKey("wrongKey");

        assertThrows(RuntimeException.class, () -> {
            restrictionService.restrict(requestDTO);
        });
    }

    @Test
    public void testMakeDTOList(){
        List<Restriction> restrictions=new ArrayList<>();
        restrictions.add(restriction1);
        restrictions.add(restriction2);
        restrictions.add(restriction3);

        RestrictionResponseDTOList responseDTOList=restrictionService.makeDTOList(restrictions);
        assertEquals(expectedResponseDTOList.getNumber(),responseDTOList.getNumber());
        List<RestrictionResponseDTO> responseDTOs=responseDTOList.getDtoList();
        for(int i=0;i<expectedResponseDTOList.getNumber();i++){
            RestrictionResponseDTO responseDTO=responseDTOs.get(i);
            RestrictionResponseDTO expectedDTO=expectedDTOList.get(i);
            assertEquals(expectedDTO.getUserId(),responseDTO.getUserId(), "사용자의 아이디 일치 필요 ");
            assertEquals(expectedDTO.getExpireTime(),responseDTO.getExpireTime(), "제제 만료 기간 일치 필요");
            assertEquals(expectedDTO.getIsExpired(),responseDTO.getIsExpired(),"제제 만료 여부 일치 필요");
            assertEquals(expectedDTO.getReason(),responseDTO.getReason(),"제제 이유 일치 필요");
            assertEquals(expectedDTO.getSuspendedFunctions(),responseDTO.getSuspendedFunctions(),"제제 기능 일치 필요");
        }
    }


    @Test
    public void testGetIsRestricted(){
        LocalDateTime now;

        Restriction r1=Restriction.builder()
            .userId(1L)
            .reason("reason1")
            .durationDays(15L)
            .triggeredTime(LocalDateTime.of(2024,12,21,0,0))
            .suspendedFunctions(1)
            .build();

        Restriction r2=Restriction.builder()
            .userId(1L)
            .reason("reason2")
            .durationDays(10L)
            .triggeredTime(LocalDateTime.of(2024,12,21,0,0))
            .suspendedFunctions(3)
            .build();

        Restriction r3=Restriction.builder()
            .userId(1L)
            .reason("reason3")
            .durationDays(5L)
            .triggeredTime(LocalDateTime.of(2024,12,21,0,0))
            .suspendedFunctions(7)
            .build();

        Restriction r4=Restriction.builder()
            .userId(1L)
            .reason("reason4")
            .durationDays(1L)
            .triggeredTime(LocalDateTime.of(2024,12,21,0,0))
            .suspendedFunctions(15)
            .build();

        
        // *데이터 넣기

        Long userId=1L;
        List<Restriction> expectedRestrictions=new ArrayList<>();
        expectedRestrictions.add(r1);
        expectedRestrictions.add(r2);
        expectedRestrictions.add(r3);
        expectedRestrictions.add(r4);

        given(restrictionRepository.findAllByUserId(userId)).willReturn(expectedRestrictions);
        // *데이터 넣기 


        //제제 시점을 기준으로 14일 뒤(1), 9일 뒤(2), 4일 뒤(3)와 정확히 제제당한 시점(4)에서 테스트 진행
        // (1) 제제 : LOGIN
        now=LocalDateTime.of(2024,12,21,0,0).plusDays(14);
        try (var mockedTimeOptimizer = mockStatic(TimeOptimizer.class)){
            mockedTimeOptimizer.when(TimeOptimizer::now).thenReturn(now);
            assertTrue(restrictionService.getIsRestricted(Function.LOGIN, userId) instanceof String);
            assertFalse((Boolean)restrictionService.getIsRestricted(Function.ARTICLE,userId));
            assertFalse((Boolean)restrictionService.getIsRestricted(Function.COMMENT,userId));
            assertFalse((Boolean)restrictionService.getIsRestricted(Function.GROUP,userId));
        }
        // (2) 제제 : LOGIN, ARTICLE
        now=LocalDateTime.of(2024,12,21,0,0).plusDays(9);
        try (var mockedTimeOptimizer = mockStatic(TimeOptimizer.class)){
            mockedTimeOptimizer.when(TimeOptimizer::now).thenReturn(now);
            assertTrue(restrictionService.getIsRestricted(Function.LOGIN, userId) instanceof String);
            assertTrue((Boolean)restrictionService.getIsRestricted(Function.ARTICLE,userId));
            assertFalse((Boolean)restrictionService.getIsRestricted(Function.COMMENT,userId));
            assertFalse((Boolean)restrictionService.getIsRestricted(Function.GROUP,userId));
        }

        // (3) 제제 : LOGIN, ARTICLE, COMMENT
        now=LocalDateTime.of(2024,12,21,0,0).plusDays(4);
        try (var mockedTimeOptimizer = mockStatic(TimeOptimizer.class)){
            mockedTimeOptimizer.when(TimeOptimizer::now).thenReturn(now);
            assertTrue(restrictionService.getIsRestricted(Function.LOGIN, userId) instanceof String);
            assertTrue((Boolean)restrictionService.getIsRestricted(Function.ARTICLE,userId));
            assertTrue((Boolean)restrictionService.getIsRestricted(Function.COMMENT,userId));
            assertFalse((Boolean)restrictionService.getIsRestricted(Function.GROUP,userId));
        }

        // (4) 제제 : LOGIN, ARTICLE, COMMENT, GROUP
        now=LocalDateTime.of(2024,12,21,0,0);
        try (var mockedTimeOptimizer = mockStatic(TimeOptimizer.class)){
            mockedTimeOptimizer.when(TimeOptimizer::now).thenReturn(now);
            assertTrue(restrictionService.getIsRestricted(Function.LOGIN, userId) instanceof String);
            assertTrue((Boolean)restrictionService.getIsRestricted(Function.ARTICLE,userId));
            assertTrue((Boolean)restrictionService.getIsRestricted(Function.COMMENT,userId));
            assertTrue((Boolean)restrictionService.getIsRestricted(Function.GROUP,userId));
        }
    }

    @Test
    public void testMakeLoginRestrictionDetail(){
        Restriction r1=Restriction.builder()
        .userId(1L)
        .reason("reason1")
        .durationDays(15L)
        .triggeredTime(LocalDateTime.of(2024,12,21,0,0))
        .suspendedFunctions(1)
        .build();

        Restriction r2=Restriction.builder()
            .userId(1L)
            .reason("reason2")
            .durationDays(10L)
            .triggeredTime(LocalDateTime.of(2024,12,21,0,0))
            .suspendedFunctions(3)
            .build();

        Restriction r3=Restriction.builder()
            .userId(1L)
            .reason("reason3")
            .durationDays(5L)
            .triggeredTime(LocalDateTime.of(2024,12,21,0,0))
            .suspendedFunctions(7)
            .build();

        Restriction r4=Restriction.builder()
            .userId(1L)
            .reason("reason4")
            .durationDays(1L)
            .triggeredTime(LocalDateTime.of(2024,12,21,0,0))
            .suspendedFunctions(15)
            .build();

        Long userId=1L;
        List<Restriction> restrictions=new ArrayList<>();
        restrictions.add(r1);
        restrictions.add(r2);
        restrictions.add(r3);
        restrictions.add(r4);
    
        given(restrictionRepository.findAllByUserId(userId)).willReturn(restrictions);
        LocalDateTime now=LocalDateTime.of(2024,12,21,0,0);
        try (var mockedTimeOptimizer = mockStatic(TimeOptimizer.class)){
            mockedTimeOptimizer.when(TimeOptimizer::now).thenReturn(now);
            Object result=restrictionService.makeLoginRestrictionDetail(userId);
            assertTrue(result instanceof String);
            assertEquals("LoginFunctionRestricted:expiredAt:2025-01-05T00:00", (String)result);
        } 

    
        
        

    }


  
}
