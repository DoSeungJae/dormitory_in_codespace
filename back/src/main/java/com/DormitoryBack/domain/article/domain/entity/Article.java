package com.DormitoryBack.domain.article.domain.entity;

import com.DormitoryBack.domain.article.domain.dto.NewArticleDTO;
import com.DormitoryBack.domain.member.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@Data
@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Document(collection = "articles")
public class Article {

    @Transient
    public static final String SEQUENCE_NAME = "articleid";
    
    @Id
    private Long id;

    private Long dormId ; 

    private String title;

    private String contentHTML; //PreviewDTO에선 text만 저장하므로 HTML에서 text만 추출하는 메서드가 필요함

    private LocalDateTime createdTime; 

    private String category;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="usr_id") //테스트 이후 외래키 설정 필요. (외래키 설정 이전에 jwt 인증)
    private User usrId;

    @JsonProperty("userId")
    public Long getUserId(){
        return usrId.getId();
    }

    public void update(NewArticleDTO dto){
        this.dormId=dto.getDormId();
        this.title=dto.getTitle();
        this.contentHTML=dto.getContentHTML();
        this.category=dto.getCategory();
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
