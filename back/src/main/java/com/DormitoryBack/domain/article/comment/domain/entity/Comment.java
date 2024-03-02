package com.DormitoryBack.domain.article.comment.domain.entity;

import com.DormitoryBack.domain.article.comment.domain.dto.CommentUpdateDTO;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.member.entity.User;
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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="article_id")
    private Article article;

    @JsonProperty
    public Long getArticleId(){
        return article ==null? null : article.getId();
    }

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    //JsonProperty가 필요하지 않을까?

    @JsonProperty
    public User getUser(){return user;}

    @Column(nullable = false,name="content")
    private String content;

    @CreatedDate
    @Column(name="time")
    private LocalDateTime createdTime;

    @Column(name="updated")
    //jsonView?
    private Boolean isUpdated;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "rootComment")
    private Set<Comment> replyComments=new HashSet<>();

    @JsonProperty
    public Set<Comment> getReplyComments(){
        return replyComments;
    }

    @JsonIgnore
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="root_comment_id")
    private Comment rootComment=null;

    public void addReplyComment(Comment replyComment){
        this.replyComments.add(replyComment);
        replyComment.setRootComment(this);
    }

    public void deleteReplyComment(Comment replyComment){
        this.replyComments.remove(replyComment);
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
