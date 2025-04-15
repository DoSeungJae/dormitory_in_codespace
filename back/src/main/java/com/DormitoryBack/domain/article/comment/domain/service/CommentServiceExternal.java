package com.DormitoryBack.domain.article.comment.domain.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import com.DormitoryBack.domain.article.comment.domain.entity.OrphanComment;
import com.DormitoryBack.domain.article.comment.domain.repository.CommentRepository;
import com.DormitoryBack.domain.article.comment.domain.repository.OrphanCommentRepository;
import com.DormitoryBack.domain.member.domain.entity.DeletedUser;
import com.DormitoryBack.domain.member.domain.entity.User;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommentServiceExternal {
    
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private OrphanCommentRepository orphanCommentRepository;
    
    public List<Comment> getUserComments(User user){
        List<Comment> userComments=commentRepository.findAllByUser(user); 
        return userComments;
    }

    public void makeAllUserCommentsOrphans(DeletedUser deletedUser, User user, List<Comment> userComments){
        userComments.stream()
            .forEach(comment -> {
                Long commentId=comment.getId();
                Long articleId=comment.getArticleId();
                OrphanComment orphanComment=OrphanComment.builder()
                    .id(commentId)
                    .comment(comment)
                    .deletedUser(deletedUser)
                    .articleId(articleId)
                    .build();
                
                orphanCommentRepository.save(orphanComment);
            });
    }

}
