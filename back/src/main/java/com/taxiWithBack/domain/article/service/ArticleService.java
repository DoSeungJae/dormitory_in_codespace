package com.taxiWithBack.domain.article.service;

import com.taxiWithBack.domain.article.dto.ArticleDTO;
import com.taxiWithBack.domain.article.entity.Article;
import com.taxiWithBack.domain.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    //Token provider가 필요한가?
    @Transactional
    public Article newArticle(ArticleDTO dto) {
        Article newOne = Article.builder()
                .dorId(dto.getDorId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(dto.getCategory())
                .createTime(dto.getCreateTime())
                .appointedTime(null)
                .usrId(null)
                .build();

        Article saved = articleRepository.save(newOne);
        return saved;

    }

    public Article getArticle(Long articleId){
        Article article=articleRepository.findByArticleId(articleId);
        if(article==null){
            throw new IllegalArgumentException("존재하지 않는 글 번호입니다.");
        }
        return article;

    }
    public List<Article> getAllArticles(){
        List<Article> articles=articleRepository.findAll();
        if(articles.isEmpty()){
            throw new RuntimeException("글이 존재하지 않습니다.");
        }
        return articles;

    }

    public List<Article> getDorArticles(Long dorId){
        List<Article> dorArticles=articleRepository.findAllByDorId(dorId);
        if(dorArticles.isEmpty()){
            throw new RuntimeException("유효하지 않는 기숙사 번호이거나 글이 존재하지 않습니다.");
        }
        return dorArticles;
    }

    @Transactional
    public Article updateArticle(ArticleDTO dto,Long articleId){
        Article article=articleRepository.findByArticleId(articleId);
        article.update(dto);
        Article saved=articleRepository.save(article);

        if(article==null){
            throw new IllegalArgumentException("존재하지 않는 글입니다.");
        }
        return saved;
    }

    @Transactional
    public void deleteArticle(Long articleId){
        Article target=articleRepository.findByArticleId(articleId);
        if(target==null){
            throw new IllegalArgumentException("존재하지 않는 글입니다.");
        }
        articleRepository.delete(target);

    }



}
