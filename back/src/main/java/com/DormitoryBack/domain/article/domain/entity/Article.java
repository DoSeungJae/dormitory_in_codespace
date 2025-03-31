package com.DormitoryBack.domain.article.domain.entity;

import com.DormitoryBack.domain.article.domain.dto.NewArticleDTO;
import com.DormitoryBack.domain.block.entity.Blockable;
import com.DormitoryBack.domain.member.domain.entity.User;
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
public class Article implements Blockable{

    @Transient
    public static final String SEQUENCE_NAME = "articleid";
    
    @Id
    private Long id;

    private Long dormId ; 

    private String title;

    private String contentHTML; //PreviewDTO에선 text만 저장하므로 HTML에서 text만 추출하는 메서드가 필요함 <- ?? (24.12.11)

    private LocalDateTime createdTime; 

    private String category;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="usr_id") 
    private User usrId; //데이터 필드 이름을 user로 바꿔야할 수도 있음. -> front가 이 변경에 의존적일 것임.

    @Override
    @JsonProperty("userId")
    public Long getUserId(){
        return usrId.getId();
    }

    private Long userId;
    
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
