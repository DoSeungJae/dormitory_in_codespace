package com.DormitoryBack.domain.article.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.DormitoryBack.domain.article.domain.dto.ArticleDTO;
import com.DormitoryBack.domain.article.domain.dto.NewArticleDTO;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.article.domain.repository.ArticleRepository;
import com.DormitoryBack.domain.article.domain.service.ArticleService;
import com.DormitoryBack.domain.auth.oauth.domain.enums.ProviderType;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.dto.UserResponseDTO;
import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.enums.RoleType;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;
import com.DormitoryBack.domain.member.restriction.domain.enums.Function;
import com.DormitoryBack.domain.member.restriction.domain.service.RestrictionService;
import com.DormitoryBack.module.TimeOptimizer;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Slf4j
public class ArticleServiceTest {

    @Mock
    private RestrictionService restrictionService;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private ArticleRepository articleRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ArticleService articleService;

    private String validToken;

    private String invalidToken;

    private Long userId;

    private NewArticleDTO request;

    private User user;

    private Article article;

    private Long articleId;

    private UserResponseDTO userResponse;

    private ArticleDTO response;

    private Long invalidArticleId;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        validToken="validToken";
        invalidToken="invalidToken";
        userId=1L;
        articleId=1L;
        invalidArticleId=0L;
        
        user=User.builder()
            .id(userId)
            .nickName("nickname")
            .passWord("password")
            .provider(null)
            .role(RoleType.ROLE_USER)
            .dormId(1L)
            .encryptedEmail("email")
            .build();
            
        request=NewArticleDTO.builder()
            .title("title")
            .contentHTML("contentHTML")
            .category("category")
            .dormId(1L)
            .build();

        article = Article.builder()
            .dormId(request.getDormId())
            .title(request.getTitle())
            .contentHTML(request.getContentHTML())
            .category(request.getCategory())
            .createdTime(TimeOptimizer.now())
            .usrId(user) 
            .userId(user.getId())
            .build();

        userResponse=UserResponseDTO.builder()
            .id(userId)
            .nickName(user.getNickName())
            .build();
    }

    @Test
    public void testSaveNewArticle_validToken() {
        when(tokenProvider.validateToken(validToken)).thenReturn(true);
        when(tokenProvider.getUserIdFromToken(validToken)).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        Article article = articleService.saveNewArticle(request, validToken);
        
        assertEquals(article.getTitle(), request.getTitle());
        assertEquals(article.getContentHTML(), request.getContentHTML());
        assertEquals(article.getCategory(), request.getCategory());
        assertEquals(article.getDormId(), request.getDormId());

        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    public void testSaveNewArticle_invalidToken(){
        when(tokenProvider.validateToken(invalidToken)).thenReturn(false);

        RuntimeException exception=assertThrows(RuntimeException.class, ()->{
            articleService.saveNewArticle(request,invalidToken);
        });

        assertEquals("유효하지 않은 토큰입니다.", exception.getMessage());
        //"InvalidToken"으로 변경 필요 
    }

    @Test
    public void testGetArticle_validArticleId(){
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        when(userRepository.findById(article.getUserId())).thenReturn(Optional.of(user));

        ArticleDTO response=articleService.getArticle(articleId);

        assertEquals(response.getId(), article.getId());
        assertEquals(response.getTitle(), article.getTitle());
        assertEquals(response.getContentHTML(), article.getContentHTML());
        assertEquals(response.getDormId(), article.getDormId());
        assertEquals(response.getCategory(), article.getCategory());
        assertEquals(response.getUser().getId(), article.getUserId());
        assertEquals(response.getUser().getNickName(), user.getNickName());
        assertEquals(response.getCreatedTime(), article.getCreatedTime());
    }

    @Test
    public void testGetArticle_invalidArticleId(){
        when(articleRepository.findById(invalidArticleId)).thenReturn(null);
        IllegalArgumentException exception=assertThrows(IllegalArgumentException.class, ()->{
            articleService.getArticle(articleId);
        });
        assertEquals("존재하지 않는 글 번호입니다.", exception.getMessage());
    }

    @Test
    public void testCheckArticleExist(){
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        when(articleRepository.findById(invalidArticleId)).thenReturn(Optional.empty());

        Boolean resultOfValidId=articleService.checkArticleExist(articleId);
        Boolean resultOfInvalidId=articleService.checkArticleExist(invalidArticleId);

        assertTrue(resultOfValidId);
        assertFalse(resultOfInvalidId);
    }






}
