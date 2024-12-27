package com.DormitoryBack.domain.member.domain.article.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import com.DormitoryBack.domain.article.domain.service.ArticleService;
import com.DormitoryBack.domain.member.restriction.domain.enums.Function;
import com.DormitoryBack.domain.member.restriction.domain.service.RestrictionService;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Slf4j
public class ArticleServiceTest {

    @Mock
    private RestrictionService restrictionService;

    @InjectMocks
    private ArticleService articleService;


    @Test
    public void testCheckRestricted(){
        Long userId=1L;
        lenient().when(restrictionService.getIsRestricted(Function.ARTICLE, userId)).thenReturn(true);
        RuntimeException exception=assertThrows(RuntimeException.class, ()->{
            articleService.checkRestricted(userId);
        });

        assertEquals("ArticleFunctionRestricted", exception.getMessage());
    }
}
