package com.taxiWithBack.domain.article.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Getter
@Setter
public class ArticleDTO {
    private Long id;
    private Long dorId;
    private Long usrId;
    private String category;
    private String title;
    private String content;
    private Time createTime; // time or String??? 글이 작성된 시간
    private Time appointedTime; //Time or String?? 배달 약속 시간


    public ArticleDTO(Long id, Long dorId, Long usrId, String category, String title, String content, Time createTime, Time appointedTime){
        this.id=id;
        this.dorId=dorId;
        this.usrId=usrId;
        this.title=title;
        this.content=content;
        this.createTime=createTime;
        this.category=category;
        this.appointedTime=appointedTime;

    }
    public ArticleDTO(Long id, Long dorId, Long usrId, String title, String content, Time createTime){ //category,appointedTime
        this.id=id;
        this.dorId=dorId;
        this.usrId=usrId;
        this.title=title;
        this.content=content;
        this.createTime=createTime;

    }

}


