package com.DormitoryBack.domain.member.restriction.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.DormitoryBack.domain.member.restriction.domain.entity.Restriction;
import com.DormitoryBack.domain.member.restriction.domain.enums.Function;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class RestrictionTest {
    
    private Restriction restriction;

    @BeforeEach
    public void setUp(){
        restriction=Restriction.builder()
            .suspendedFunctions(0)
            .build();
    }


    @Test
    public void testIsSuspended(){
        restriction.setSuspendedFunctions(0);
        assertFalse(restriction.isSuspended(Function.LOGIN));
        assertFalse(restriction.isSuspended(Function.ARTICLE));
        assertFalse(restriction.isSuspended(Function.COMMENT));
        assertFalse(restriction.isSuspended(Function.GROUP));

        restriction.setSuspendedFunctions(1);
        assertTrue(restriction.isSuspended(Function.LOGIN));
        assertFalse(restriction.isSuspended(Function.ARTICLE));
        assertFalse(restriction.isSuspended(Function.COMMENT));
        assertFalse(restriction.isSuspended(Function.GROUP));

        restriction.setSuspendedFunctions(2);
        assertFalse(restriction.isSuspended(Function.LOGIN));
        assertTrue(restriction.isSuspended(Function.ARTICLE));
        assertFalse(restriction.isSuspended(Function.COMMENT));
        assertFalse(restriction.isSuspended(Function.GROUP));

        restriction.setSuspendedFunctions(4);
        assertFalse(restriction.isSuspended(Function.LOGIN));
        assertFalse(restriction.isSuspended(Function.ARTICLE));
        assertTrue(restriction.isSuspended(Function.COMMENT));
        assertFalse(restriction.isSuspended(Function.GROUP));

        restriction.setSuspendedFunctions(8);
        assertFalse(restriction.isSuspended(Function.LOGIN));
        assertFalse(restriction.isSuspended(Function.ARTICLE));
        assertFalse(restriction.isSuspended(Function.COMMENT));
        assertTrue(restriction.isSuspended(Function.GROUP));


        restriction.setSuspendedFunctions(3);
        assertTrue(restriction.isSuspended(Function.LOGIN));
        assertTrue(restriction.isSuspended(Function.ARTICLE));
        assertFalse(restriction.isSuspended(Function.COMMENT));
        assertFalse(restriction.isSuspended(Function.GROUP));

        restriction.setSuspendedFunctions(7);
        assertTrue(restriction.isSuspended(Function.LOGIN));
        assertTrue(restriction.isSuspended(Function.ARTICLE));
        assertTrue(restriction.isSuspended(Function.COMMENT));
        assertFalse(restriction.isSuspended(Function.GROUP));

        restriction.setSuspendedFunctions(15);
        assertTrue(restriction.isSuspended(Function.LOGIN));
        assertTrue(restriction.isSuspended(Function.ARTICLE));
        assertTrue(restriction.isSuspended(Function.COMMENT));
        assertTrue(restriction.isSuspended(Function.GROUP));
    }

    @Test
    public void testSuspendFunction(){
        restriction.suspendFunction(Function.LOGIN);
        assertTrue(restriction.isSuspended(Function.LOGIN));

        restriction.suspendFunction(Function.ARTICLE);
        assertTrue(restriction.isSuspended(Function.LOGIN));
        assertTrue(restriction.isSuspended(Function.ARTICLE));

        restriction.suspendFunction(Function.COMMENT);
        assertTrue(restriction.isSuspended(Function.LOGIN));
        assertTrue(restriction.isSuspended(Function.ARTICLE));
        assertTrue(restriction.isSuspended(Function.COMMENT));

        restriction.suspendFunction(Function.GROUP);
        assertTrue(restriction.isSuspended(Function.LOGIN));
        assertTrue(restriction.isSuspended(Function.ARTICLE));
        assertTrue(restriction.isSuspended(Function.COMMENT));
        assertTrue(restriction.isSuspended(Function.GROUP));
        
        restriction.suspendFunction(Function.COMMENT);
        assertTrue(restriction.isSuspended(Function.COMMENT));
    }

    @Test
    public void testReleaseFunction(){
        restriction.setSuspendedFunctions(15);
        assertTrue(restriction.isSuspended(Function.LOGIN));
        assertTrue(restriction.isSuspended(Function.ARTICLE));
        assertTrue(restriction.isSuspended(Function.COMMENT));
        assertTrue(restriction.isSuspended(Function.GROUP));   

        restriction.releaseFunction(Function.GROUP);
        assertFalse(restriction.isSuspended(Function.GROUP));
        assertEquals(7,restriction.getSuspendedFunctions());

        restriction.releaseFunction(Function.LOGIN);
        assertFalse(restriction.isSuspended(Function.LOGIN));
        assertEquals(6,restriction.getSuspendedFunctions());
    }

    @Test
    public void testSuspendedFunctionsAsStringList(){
        List<String> expectedFunctions;

        restriction.setSuspendedFunctions(3);
        expectedFunctions=Arrays.asList("LOGIN","ARTICLE");
        assertEquals(expectedFunctions,restriction.getSuspendedFunctionsAsStringList());

        restriction.suspendFunction(Function.GROUP);
        expectedFunctions=Arrays.asList("GROUP","LOGIN","ARTICLE");
        assertNotEquals(expectedFunctions, restriction.getSuspendedFunctionsAsStringList());

        expectedFunctions=Arrays.asList("LOGIN","ARTICLE","GROUP");
        assertEquals(expectedFunctions, restriction.getSuspendedFunctionsAsStringList());


        
    }

}
