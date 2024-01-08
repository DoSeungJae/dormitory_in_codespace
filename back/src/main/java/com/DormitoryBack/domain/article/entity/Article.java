package com.DormitoryBack.domain.article.entity;
import com.DormitoryBack.domain.article.dto.ArticleDTO;
import com.DormitoryBack.domain.member.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Table(name="article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(nullable=false,name="dor_id")
    private Long dorId ;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="usr_id") //테스트 이후 외래키 설정 필요. (외래키 설정 이전에 jwt 인증)
    private User usrId;

    @JsonProperty("userId")
    public Long getUserId(){
        return usrId.getId();
    }

    @Column(nullable=false,name="title")
    private String title;
    @Column(nullable=false,name="content")
    private String content;

    @CreatedDate
    @Column(nullable=false,name="create_time")
    private LocalDateTime createTime;
    @Column(name="category")
    private String category;
    @Column(name="appointed_time") //추후에 설정.
    private LocalDateTime appointedTime;

    public void update(ArticleDTO dto){
        this.dorId=dto.getDorId();
        this.title=dto.getTitle();
        this.content=dto.getContent();
        this.category=dto.getCategory();
        this.createTime=dto.getCreateTime();
        //this.appointedTime=dto.getAppointedTime();
    }

    @Override
    public String toString(){
        return "articleId:"+this.id+", dormitoryId:"+this.dorId+", userId:"+this.usrId.getId()+", title:"+title+", content:"+content+", createTime:"+createTime+
                ", category:"+category+", appointedTime:"+appointedTime;
    }

    public String toJsonString(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String jsonString = "";
        try {
            jsonString = objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
