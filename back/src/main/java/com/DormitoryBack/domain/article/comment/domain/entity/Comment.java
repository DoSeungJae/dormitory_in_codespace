package com.DormitoryBack.domain.article.comment.domain.entity;

import com.DormitoryBack.domain.article.comment.domain.dto.CommentUpdateDTO;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.member.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name="comment")
public class Comment {

    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    @JoinColumn(name="article_id")
    private Long articleId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @JsonProperty
    public User getUser(){return user;}

    @Column(nullable = false,name="content")
    private String content;

    @CreatedDate
    @Column(name="time",nullable = false)
    private LocalDateTime createdTime;

    @Column(name="updated")
    private Boolean isUpdated;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="root_comment_id")
    private Comment rootComment;

    @JsonProperty
    public Long getRootCommentId(){
        return rootComment==null ? null : rootComment.getId();
    }
    public void addReplyComment(Comment replyComment){
        replyComment.setRootComment(this);
    }

    public void update(CommentUpdateDTO dto){
        this.isUpdated=true;
        this.content=dto.getContent();
    }
    public String toJsonString(){
        ObjectMapper objectMapper=new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonString="";
        try{
            jsonString=objectMapper.writeValueAsString(this);
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return jsonString;
    }


}
