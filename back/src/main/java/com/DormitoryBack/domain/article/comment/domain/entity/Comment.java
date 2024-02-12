package com.DormitoryBack.domain.article.comment.domain.entity;

import com.DormitoryBack.domain.article.comment.domain.dto.CommentUpdateDTO;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.member.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    //굳이 필요할까?

    @JsonProperty
    public Long getArticleId(){
        return article.getId();
    }
    @Column(nullable = false,name="content")
    private String content;

    @CreatedDate
    @Column(name="time")
    private LocalDateTime createdTime;

    @Column(name="updated")
    private Boolean isUpdated=false;

    public void update(CommentUpdateDTO dto){
        this.isUpdated=dto.getIsUpdated();
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
