package com.taxiWithBack.domain.article.service;


import com.taxiWithBack.domain.article.dto.ArticleDTO;
import com.taxiWithBack.domain.article.entity.Article;
import com.taxiWithBack.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    //Token provider가 필요한가?
    public Article newArticle(ArticleDTO dto){
        Article newOne=Article.builder()
                .id(dto.getId())
                .dorId(dto.getDorId())
                .usrId(dto.getUsrId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .createTime(dto.getCreateTime())
                .category(dto.getCategory())
                .appointedTime(dto.getAppointedTime())
                .build();

        Article saved=articleRepository.save(newOne);
        return saved;

    }


}
