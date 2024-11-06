package com.DormitoryBack.domain.article.domain.dto;

import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class NewArticleDTO {

    private String title;
    
    private String contentHTML;

    private Long dormId;
    
    private String category;
    
    private LocalDateTime createdTime;
    
    public NewArticleDTO(){}

    public NewArticleDTO(Long dormId, String category, String title, String contentHTML, LocalDateTime createdTime){
        this.dormId=dormId;
        this.title=title;
        this.contentHTML=contentHTML;
        this.category=category;
        this.createdTime=createdTime;
    }

    public NewArticleDTO(Long dormId, String title, String contentHTML, LocalDateTime createdTime){ 
        this.dormId=dormId;
        this.title=title;
        this.contentHTML=contentHTML;
        this.createdTime=createdTime;
    }
    //굳이 createdTime이 들어가야 하나?

}


