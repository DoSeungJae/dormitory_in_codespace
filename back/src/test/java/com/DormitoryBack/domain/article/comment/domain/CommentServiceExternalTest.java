package com.DormitoryBack.domain.article.comment.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import com.DormitoryBack.domain.article.comment.domain.entity.OrphanComment;
import com.DormitoryBack.domain.article.comment.domain.repository.CommentRepository;
import com.DormitoryBack.domain.article.comment.domain.repository.OrphanCommentRepository;
import com.DormitoryBack.domain.article.comment.domain.service.CommentServiceExternal;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.article.domain.service.ArticleService;
import com.DormitoryBack.domain.member.domain.entity.DeletedUser;
import com.DormitoryBack.domain.member.domain.entity.User;

@ExtendWith(MockitoExtension.class)
public class CommentServiceExternalTest {

    @InjectMocks
    private CommentServiceExternal commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private OrphanCommentRepository orphanCommentRepository;

    @Mock
    private ArticleService articleService;

    User user=User.builder()
        .dormId(3L)
        .encryptedEmail("encrypted")
        .id(1L)
        .imageName("image")
        .nickName("nick")
        .passWord("password")
        .build();

    Comment comment1=Comment.builder()
        .articleId(2L)
        .content("content1")
        .createdTime(LocalDateTime.now())
        .id(1L)
        .user(user)
        .build();

    Comment comment2=Comment.builder()
        .articleId(2L)
        .content("content2")
        .createdTime(LocalDateTime.now())
        .id(2L)
        .user(user)
        .build();

    Comment comment3=Comment.builder()
        .articleId(2L)
        .content("content3")
        .createdTime(LocalDateTime.now())
        .id(3L)
        .user(user)
        .build();

    Comment comment4=Comment.builder()
        .articleId(2L)
        .content("content4")
        .createdTime(LocalDateTime.now())
        .id(4L)
        .user(user)
        .build();

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetUserComments(){

        List<Comment> expectedUserComments=List.of(comment1, comment2, comment3, comment4);

        when(commentRepository.findAllByUser(user)).thenReturn(expectedUserComments);

        List<Comment> actualUserComments=commentService.getUserComments(user);

        assertEquals(expectedUserComments, actualUserComments);
    }

    @Test
    public void testMakeAllUserCommentsOrphans(){

        DeletedUser deletedUser=DeletedUser.builder()
            .dormId(3L)
            .encryptedEmail("encrypted")
            .id(1L)
            .imageName("image")
            .nickName("nick")
            .passWord("password")
            .build();

        Article article=Article.builder()
            .id(2L)
            .title("title")
            .contentHTML("content")
            .createdTime(LocalDateTime.now())
            .usrId(user)
            .userId(user.getId())
            .build();


        List<Comment> userComments=List.of(comment1, comment2, comment3, comment4);

        commentService.makeAllUserCommentsOrphans(deletedUser, user, userComments);
        
        int size=userComments.size();
        ArgumentCaptor<OrphanComment> argumentCaptor=ArgumentCaptor.forClass(OrphanComment.class);
        verify(orphanCommentRepository,times(size)).save(argumentCaptor.capture());

        List<OrphanComment> savedOrphanComments=argumentCaptor.getAllValues();
        assertEquals(size,savedOrphanComments.size());

        assertEquals(1L, savedOrphanComments.get(0).getId());
        assertEquals(comment1, savedOrphanComments.get(0).getComment());
        assertEquals(deletedUser, savedOrphanComments.get(0).getDeletedUser());
        assertEquals(article.getId(),savedOrphanComments.get(0).getArticleId());

        assertEquals(2L, savedOrphanComments.get(1).getId());
        assertEquals(comment2, savedOrphanComments.get(1).getComment());
        assertEquals(deletedUser, savedOrphanComments.get(1).getDeletedUser());
        assertEquals(article.getId(),savedOrphanComments.get(1).getArticleId());

        assertEquals(3L, savedOrphanComments.get(2).getId());
        assertEquals(comment3, savedOrphanComments.get(2).getComment());
        assertEquals(deletedUser, savedOrphanComments.get(2).getDeletedUser());
        assertEquals(article.getId(),savedOrphanComments.get(2).getArticleId());

        assertEquals(4L, savedOrphanComments.get(3).getId());
        assertEquals(comment4, savedOrphanComments.get(3).getComment());
        assertEquals(deletedUser, savedOrphanComments.get(3).getDeletedUser());
        assertEquals(article.getId(),savedOrphanComments.get(3).getArticleId());
        


    }
}
