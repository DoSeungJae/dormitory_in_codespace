package com.DormitoryBack.domain.article.comment.domain.service;

import com.DormitoryBack.domain.article.comment.domain.dto.CommentDTO;
import com.DormitoryBack.domain.article.comment.domain.dto.CommentReplyDTO;
import com.DormitoryBack.domain.article.comment.domain.dto.CommentReplyResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
            throw new RuntimeException("NoCommentFound");
        }
        return comments;
    }
    public List<Comment> getUserComments(Long userId){
        User user=userRepository.findById(userId).orElse(null);
        if(user==null){
            throw new RuntimeException("UserNotFound");
        }
        List<Comment> comments=commentRepository.findAllByUser(user);
        if(comments.isEmpty()){
            throw new RuntimeException("NoCommentFound");
        }
        return comments;
    }
    public List<Comment> getArticleComments(Long articleId){
        Article article=articleRepository.findById(articleId).orElse(null);
        if(article==null){
            throw new RuntimeException("ArticleNotFound");
        }
        List<Comment> comments=commentRepository.findAllByArticle(article);
        if(comments.isEmpty()){
            throw new RuntimeException("NoCommentFound");
        }
        return comments;
    }

    public Page<Comment> getArticleCommentsPerPage(int page,int size){
        Pageable pageable= PageRequest.of(page,size, Sort
                .by("createdTime")
                .ascending());
        Page<Comment> commentPage=commentRepository.findAll(pageable);
        if(commentPage.isEmpty()){
            throw new RuntimeException("NoMoreCommentPage");
        }
        return commentPage;
    }
    public List<String> listStringify(List<Comment> commentList){
        List<String> stringifiedCommentList=commentList.stream()
                .map(Comment::toJsonString)
                .collect(Collectors.toList());

        return stringifiedCommentList;
    }
    @Transactional
    public Comment newComment(CommentDTO dto,String token){
        if (!tokenProvider.validateToken(token)) {
            throw new JwtException("InvalidToken");
        }

        Long userId=tokenProvider.getUserIdFromToken(token);
        User userData=userRepository.findById(userId).orElse(null);
        Article article=articleRepository.findById(dto.getArticleId()).orElse(null);


        Comment newComment=Comment.builder()
                .article(article)
                .user(userData)
                .content(dto.getContent())
                .createdTime(LocalDateTime.now())
                .isUpdated(false)
                .build();
        Comment saved=commentRepository.save(newComment);
        return saved;

    }

    @Transactional
    public CommentReplyResponseDTO newReply(CommentReplyDTO dto, String token) {
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("InvalidToken");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        User userData=userRepository.findById(userId).orElse(null);
        Comment rootComment=commentRepository.findById(dto.getRootCommentId()).orElse(null);
        if(rootComment==null){
            throw new RuntimeException("CommentNotFound");
        }
        if(rootComment.getRootComment()!=null){
            throw new RuntimeException("CannotReplyToReplies");
        }
        Comment newReply=Comment.builder()
                .article(null)
                .user(userData)
                .content(dto.getContent())
                .createdTime(LocalDateTime.now())
                .isUpdated(false)
                .build();

        rootComment.addReplyComment(newReply);
        Comment saved=commentRepository.save(newReply);
        CommentReplyResponseDTO commentResponseDTO= CommentReplyResponseDTO.builder()
                .content(saved.getContent())
                .time(saved.getCreatedTime())
                .rootCommentId(saved.getRootComment().getId())
                .build();

        return commentResponseDTO;
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
        if(comment.getUser().getId()!=tokenProvider.getUserIdFromToken(token)){
            throw new RuntimeException("NoPermission");
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
        Comment comment=commentRepository.findById(commentId).orElse(null);
        if(comment.getUser().getId()!=tokenProvider.getUserIdFromToken(token)){
            throw new RuntimeException("NoPermission");
        }
        commentRepository.delete(target);
    }

    public List<String> pageStringify(Page<Comment> commentPage){
        List<String> stringifiedCommentList=commentPage.getContent()
                .stream()
                .map(Comment::toJsonString)
                .collect(Collectors.toList());

        return stringifiedCommentList;
    }


}
