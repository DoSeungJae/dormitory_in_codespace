package com.DormitoryBack.domain.article.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NewArticleDTO {

    private String title;
    
    private String contentHTML;

    private Long dormId;
    
    private String category;
    
    public NewArticleDTO(){}

    public NewArticleDTO(Long dormId, String category, String title, String contentHTML){
        this.dormId=dormId;
        this.title=title;
        this.contentHTML=contentHTML;
        this.category=category;
    }

    public NewArticleDTO(Long dormId, String title, String contentHTML){ 
        this.dormId=dormId;
        this.title=title;
        this.contentHTML=contentHTML;
    }


}


