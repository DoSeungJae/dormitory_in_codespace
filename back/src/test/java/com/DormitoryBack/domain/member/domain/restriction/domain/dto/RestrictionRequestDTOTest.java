package com.DormitoryBack.domain.member.domain.restriction.domain.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.DormitoryBack.domain.member.restriction.domain.dto.RestrictionRequestDTO;
import com.DormitoryBack.domain.member.restriction.domain.enums.Function;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class RestrictionRequestDTOTest {    

    @Test
    public void testGetSuspensionFunctionsAsEnum(){
        List<String> suspendedFunctions=Arrays.asList("LOGIN","ARTICLE");

        RestrictionRequestDTO requestDTO=RestrictionRequestDTO.builder()
            .suspendedFunctions(suspendedFunctions)
            .build();

        List<Function> expectedFunctions=Arrays.asList(Function.LOGIN,Function.ARTICLE);
        List<Function> actualFunctions=requestDTO.getSuspensionFunctionsAsEnum();

        assertEquals(expectedFunctions, actualFunctions);

    }
    
}
