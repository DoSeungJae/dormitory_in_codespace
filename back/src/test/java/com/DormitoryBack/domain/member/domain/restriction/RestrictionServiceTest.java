package com.DormitoryBack.domain.member.domain.restriction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionRequestDTO;
import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionResponseDTO;
import com.DormitoryBack.domain.member.restriction.domain.entity.Restriction;
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

    @InjectMocks
    private RestrictionService restrictionService;

    private Restriction restriction;

    private RestrictionRequestDTO requestDTO;

    @Value("administrator.key")
    private String key;

    @BeforeEach
    public void setUp(){
        restriction= Restriction.builder()
            .userId(1L)
            .reason("test reason")
            .durationDays(10L)
            .triggeredTime(LocalDateTime.of(2024,12,21,0,0))
            .build();

        requestDTO=RestrictionRequestDTO.builder()
            .accessKey("20220393-2470011192")
            .userId(1L)
            .reason("test reason")
            .durationDays(10L)
            .build();
    }

    @Test
    public void testMakeDTO(){
        LocalDateTime now= LocalDateTime.of(2024,12,31,0,0,1);
        try (var mockedTimeOptimizer = mockStatic(TimeOptimizer.class)){
            mockedTimeOptimizer.when(TimeOptimizer::now).thenReturn(now);

            RestrictionResponseDTO dto=restrictionService.makeDTO(restriction);
    
    
            assertEquals(restriction.getUserId(), dto.getUserId());
            assertEquals(restriction.getTriggeredTime().plusDays(restriction.getDurationDays()),dto.getExpireTime());
            assertTrue(dto.getIsExpired());
            assertEquals(restriction.getReason(), dto.getReason());
        }    
    }


}
