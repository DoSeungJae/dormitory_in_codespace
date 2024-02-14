package com.DormitoryBack.domain.report.entity;


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
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name="report")
public class Report {
    @Id
    @GeneratedValue
    @Column(name="id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="reporter_id")
    private User reporter;
    @JsonProperty
    public Long getUserId(){return reporter.getId();}

    @Column(nullable=false,name="is_article")
    private Boolean isArticle;
    //true면 article에 대한 신고, false면 comment에 대한 신고임

    @Column(name="post_id")
    private Long postId;
    //isArticle이 1이면 articleId,
    //0이면 commentId로 인식

    @CreatedDate
    @Column(name="time")
    private LocalDateTime time;

    @Column(name="reason")
    private String reason;
    //도배, 음란물, 상업적, 정치, 노쇼, 욕설, 비하

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
