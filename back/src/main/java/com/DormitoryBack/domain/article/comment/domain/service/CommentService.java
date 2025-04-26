package com.DormitoryBack.domain.article.comment.domain.service;

import com.DormitoryBack.domain.article.comment.domain.dto.*;
import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import com.DormitoryBack.domain.article.comment.domain.entity.OrphanComment;
import com.DormitoryBack.domain.article.comment.domain.repository.CommentRepository;
import com.DormitoryBack.domain.article.comment.domain.repository.OrphanCommentRepository;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.article.domain.repository.ArticleRepository;
import com.DormitoryBack.domain.block.service.BlockService;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.domain.entity.User;
import com.DormitoryBack.domain.member.domain.repository.UserRepository;
import com.DormitoryBack.domain.member.domain.service.UserServiceExternal;
import com.DormitoryBack.domain.notification.constant.NotificationConstants;
import com.DormitoryBack.domain.notification.dto.Notifiable;
import com.DormitoryBack.domain.notification.enums.EntityType;
import com.DormitoryBack.domain.notification.service.NotificationServiceExternal;
import com.DormitoryBack.exception.ErrorInfo;
import com.DormitoryBack.exception.ErrorType;
import com.DormitoryBack.exception.globalException.EntityNotFoundException;
import com.DormitoryBack.module.TimeOptimizer;
import com.DormitoryBack.module.xssFilter.XSSFilter;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
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

    @Autowired
    private NotificationServiceExternal notificationService;

    @Autowired
    private BlockService blockService;

    @Autowired
    private UserServiceExternal userService;

    @Autowired
    private OrphanCommentRepository orphanCommentRepository;

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

    public CommentResponseDTO getArticleComments(Long articleId, String token){
        Article article=articleRepository.findById(articleId).orElse(null);
        if(article==null){
            throw new RuntimeException("ArticleNotFound");
        }
        if(token==null){
            throw new RuntimeException("InvalidToken");
        }
        List<Comment> commentList=commentRepository.findAllByArticleId(articleId);
        List<Long> blockedIdList=blockService.getBlockedIdList(token);
        
        List<Comment> rootComments=new ArrayList<>();
        List<Comment> replyComments=new ArrayList<>();
        Iterator<Comment> iterator=commentList.iterator();
        while(iterator.hasNext()){
            Comment comment=iterator.next();
            if(isCommentBlocked(comment, blockedIdList)){
                continue;
            }

            if(comment.getUser()==null){ //댓글의 사용자가 탈퇴한 경우. "nickname" "-"임.
                Long commentId=comment.getId();
                User deletedVirtualUser=userService.getDeletedVirtualUserFromOrphanComment(commentId);
                comment.setVirtualDeletedUser(deletedVirtualUser);
            }

            if(comment.getRootComment()==null){
                rootComments.add(comment);
            }
            else{
                replyComments.add(comment);
            }
        }
        
        Long size=Long.valueOf(rootComments.size()+replyComments.size());
        CommentResponseDTO responseDTO=CommentResponseDTO.builder()
                .number(size)
                .rootComments(listStringify(rootComments))
                .replyComments(listStringify(replyComments))
                .build();

        return responseDTO;
    }

    public Boolean isCommentBlocked(Comment comment, List<Long> blockedIdList){
        Long commentUserId;
        if(comment.getUser()==null){
            Long commentId=comment.getId();
            User deletedVirtualUser=userService.getDeletedVirtualUserFromOrphanComment(commentId);
            commentUserId=deletedVirtualUser.getId();
        }else{
            commentUserId=comment.getUser().getId();
        }
        Boolean isBlocked=blockedIdList.contains(commentUserId);
        if(comment.getRootComment()!=null){
            Long replyCommentUserId=comment.getRootComment().getUser().getId();
            isBlocked=isBlocked || blockedIdList.contains(replyCommentUserId);
        }
        return isBlocked;
    }

    /*
    public Long getNumberOfComments(Long articleId, String token){
        List<Long> blockedIdList=blockService.getBlockedIdList(token);
        List<Comment> commentList;
        if(blockedIdList.size()==0){
            commentList=commentRepository.findAllByArticleId(articleId);
        }
        else{
            commentList=commentRepository.findByArticleIdExcludingBlockedComments(articleId, blockedIdList);
            //여기서 orphan count? 
        }
        int size=commentList.size();
        return Long.valueOf(size);
    }
    */

    public Long getNumberOfComments(Long articleId, String token){
        CommentResponseDTO dto=getArticleComments(articleId, token);
        return dto.getNumber();
    }

    public List<Long> getUserCommentedArticleIds(Long userId){
        List<Long> ids=commentRepository.findDistinctArticleIdsByUserId(userId);
        return ids;
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

        String rawContent=dto.getContent();
        String safeContent=XSSFilter.filter(rawContent);

        Comment newComment=Comment.builder()
                .articleId(article.getId())
                .user(userData)
                .content(safeContent)
                .createdTime(TimeOptimizer.now())
                .isUpdated(false)
                .build();

        Comment savedComment=commentRepository.save(newComment);

        Notifiable subject=Notifiable.builder()
                .entityType(EntityType.ARTICLE)
                .entityId(article.getId())
                .stringifiedEntity(article.toJsonString())
                .build();

        Notifiable trigger=Notifiable.builder()
                .entityType(EntityType.COMMENT)
                .entityId(savedComment.getId())
                .stringifiedEntity(savedComment.toJsonString())
                .build();

        notificationService.saveAndPublishNotification(subject,trigger,String.format(NotificationConstants.NEW_COMMENT,savedComment.getContent()));

        return savedComment;
    }


    @Transactional
    public CommentReplyResponseDTO newReply(CommentReplyDTO dto, String token) {

        if(!tokenProvider.validateToken(token)){
            throw new JwtException("InvalidToken");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        User userData=userRepository.findById(userId).orElse(null);
        Comment rootComment=commentRepository.findById(dto.getRootCommentId()).orElse(null);
        Article rootArticle=articleRepository.findById(rootComment.getArticleId()).orElse(null);

        String rawContent=dto.getContent();
        String safeContent=XSSFilter.filter(rawContent);

        if(rootComment==null){
            throw new RuntimeException("CommentNotFound");
        }
        if(rootComment.getRootComment()!=null){
            throw new RuntimeException("CannotReplyToReplies");
        }
        if(rootArticle==null){
            throw new RuntimeException("ArticleNotFound");
        }

        Comment newReply=Comment.builder()
                .articleId(rootArticle.getId())
                .user(userData)
                .content(safeContent)
                .createdTime(TimeOptimizer.now())
                .build();

        rootComment.addReplyComment(newReply);
        Comment savedReply=commentRepository.save(newReply);

        Notifiable commentSubject=Notifiable.builder()
               .entityType(EntityType.COMMENT)
               .entityId(rootComment.getId())
               .stringifiedEntity(rootComment.toJsonString())
               .build();
        
        Article article=articleRepository.findById(rootComment.getArticleId()).orElse(null);
        Notifiable articleSubject=Notifiable.builder()
            .entityType(EntityType.ARTICLE)
            .entityId(rootComment.getArticleId())
            .stringifiedEntity(article.toJsonString())
            .build();

        Notifiable trigger=Notifiable.builder()
               .entityType(EntityType.COMMENT)
               .entityId(savedReply.getId())
               .stringifiedEntity(savedReply.toJsonString())
               .build();

       notificationService.saveAndPublishNotification(commentSubject,trigger,String.format(NotificationConstants.NEW_REPLY,savedReply.getContent()));
       
       //중복 검사 이후 문제 없으면 article에 notification trigger
       if(rootComment.getUser()==null){
            ;
       }
       else if(rootComment.getUser().getId()!=article.getUserId()){
            notificationService.saveAndPublishNotification(articleSubject,trigger,String.format(NotificationConstants.NEW_COMMENT,savedReply.getContent()));
       }
       

       CommentReplyResponseDTO commentResponseDTO= CommentReplyResponseDTO.builder()
                .content(String.format(NotificationConstants.NEW_REPLY,savedReply.getContent()))
                .time(savedReply.getCreatedTime())
                .rootCommentId(savedReply.getRootComment().getId())
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

        String rawContent=dto.getContent();
        String safeContent=XSSFilter.filter(rawContent);
        dto.setSafeContent(safeContent);
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

    public List<String> pageStringify(@NotNull Page<Comment> commentPage){
        List<String> stringifiedCommentList=commentPage.getContent()
                .stream()
                .map(Comment::toJsonString)
                .collect(Collectors.toList());

        return stringifiedCommentList;
    }

    public void deleteAllRootComemntsInArticle(Long articleId){
        List<Comment> commentList=commentRepository.findAllByArticleId(articleId);
        commentList.stream()
            .filter(comment -> comment.getRootComment()==null)
            .forEach(comment -> commentRepository.delete(comment));
    }

    
    public OrphanComment getOrphanComment(Long commentId){
        OrphanComment orphanComment=orphanCommentRepository
            .findById(commentId)
            .orElseThrow(()->new EntityNotFoundException(new ErrorInfo(ErrorType.EntityNotFound, "orphanComment를 찾지 못했습니다.")));
        
        return orphanComment;
    }

}
