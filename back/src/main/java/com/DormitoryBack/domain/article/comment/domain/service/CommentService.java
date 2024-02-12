package com.DormitoryBack.domain.article.comment.domain.service;

import com.DormitoryBack.domain.article.comment.domain.dto.CommentDTO;
import com.DormitoryBack.domain.article.comment.domain.dto.CommentUpdateDTO;
import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import com.DormitoryBack.domain.article.comment.domain.repository.CommentRepository;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.article.domain.repository.ArticleRepository;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.entity.User;
import com.DormitoryBack.domain.member.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenProvider tokenProvider;

    public Comment getComment(Long commentId){
        Comment comment=commentRepository.findById(commentId).orElse(null);
        if(comment==null){
            throw new IllegalArgumentException("CommentNotFound");
        }
        return comment;
    }
    public List<Comment> getAllComments(){
        List<Comment> comments=commentRepository.findAll();
        if(comments.isEmpty()){
            throw new RuntimeException("NoComment");
        }
        return comments;
    }
    public List<String> listStringify(List<Comment> commentList){
        List<String> stringifiedCommentList=commentList.stream()
                .map(Comment::toJsonString)
                .collect(Collectors.toList());

        return stringifiedCommentList;

    }
    public Comment newComment(CommentDTO dto,String token){
        if (!tokenProvider.validateToken(token)) {
            throw new JwtException("InvalidToken");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        User userData=userRepository.findById(userId).orElse(null);
        Article userArticle=articleRepository.findById(userId).orElse(null);

        Comment newComment=Comment.builder()
                .article(userArticle)
                .user(userData)
                .content(dto.getContent())
                .createdTime(dto.getCreatedTime())
                .isUpdated(false)
                .build();
        Comment saved=commentRepository.save(newComment);
        return saved;

    }
    @Transactional
    public Comment updateComment(CommentUpdateDTO dto,Long commentId, String token){
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("InvalidToken");
        }
        Comment comment=commentRepository.findById(commentId).orElse(null);
        if(comment==null){
            throw new IllegalArgumentException("CommentNotFound");
        }
        comment.update(dto);
        Comment saved=commentRepository.save(comment);

        return saved;
    }

    @Transactional
    public void deleteComment(Long commentId, String token) {
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("InvalidToken");
        }
        Comment target=commentRepository.findById(commentId).orElse(null);
        if(target==null){
            throw new IllegalArgumentException("CommentNotFound");
        }
        commentRepository.delete(target);
    }
}
