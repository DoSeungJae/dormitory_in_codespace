package com.DormitoryBack.domain.report.entity;


import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.member.entity.User;
import com.DormitoryBack.domain.report.enums.ReportType;
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
    
    @Column(name = "target_type")
    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column(name = "target_id")
    private Long targetId;

    @CreatedDate
    @Column(name="time")
    private LocalDateTime time;

    @Column(name="reason")
    private String reason;
    //도배글, 음란물, 상업적글, 정치글, 노쇼, 욕설, 비하, 문의 등 

    @Column(name="inquire_content")
    private String inquireContent;

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
