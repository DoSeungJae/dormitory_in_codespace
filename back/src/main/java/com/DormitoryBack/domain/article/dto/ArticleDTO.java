package com.DormitoryBack.domain.article.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleDTO {
    private Long dorId;
    private String category;
    private String title;
    private String content;
    private LocalDateTime createTime;

    //appointedTime 추가 필요

    public ArticleDTO(){}

    public ArticleDTO(Long dorId,  String category, String title, String content ,LocalDateTime createTime){
        this.dorId=dorId;
        this.title=title;
        this.content=content;
        this.category=category;
        this.createTime=createTime;
    }
    public ArticleDTO(Long dorId, String title, String content, LocalDateTime createTime){ //category,appointedTime
        this.dorId=dorId;
        this.title=title;
        this.content=content;
        this.createTime=createTime;
    }

}


