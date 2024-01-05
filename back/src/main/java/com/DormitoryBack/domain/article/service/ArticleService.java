package com.DormitoryBack.domain.article.service;

import com.DormitoryBack.domain.article.dto.ArticleDTO;
import com.DormitoryBack.domain.article.entity.Article;
import com.DormitoryBack.domain.article.repository.ArticleRepository;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.entity.User;
import com.DormitoryBack.domain.member.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;
    @Transactional
    public Article newArticle(ArticleDTO dto, String token) {

        if(!tokenProvider.validateToken(token)){
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

        Long usrId=tokenProvider.getUserIdFromToken(token);
        User user=userRepository.findById(usrId).orElse(null);

        Article newOne = Article.builder()
                .dorId(dto.getDorId())
                .title(dto.getTitle())
                .content(dto.getContent())
                .category(dto.getCategory())
                .createTime(dto.getCreateTime())
                .appointedTime(null)
                .usrId(user)
                .build();

        Article saved = articleRepository.save(newOne);
        return saved;

    }

    public Article getArticle(Long articleId){
        Article article=articleRepository.findById(articleId).orElse(null);
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
    public Article updateArticle(ArticleDTO dto,Long articleId,String token){
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

        Article article=articleRepository.findById(articleId).orElse(null);
        article.update(dto);
        Article saved=articleRepository.save(article);

        if(article==null){
            throw new IllegalArgumentException("존재하지 않는 글입니다.");
        }
        return saved;
    }

    @Transactional
    public void deleteArticle(Long articleId,String token){
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("유효하지 않은 토큰입니다.");
        }

        Article target=articleRepository.findById(articleId).orElse(null);
        if(target==null){
            throw new IllegalArgumentException("존재하지 않는 글입니다.");
        }
        articleRepository.delete(target);
    }

    public List<String> listStringify(List<Article> articleList){
        List<String> stringifiedArticleList=articleList.stream()
                .map(Article::toJsonString)
                .collect(Collectors.toList());

        return stringifiedArticleList;
    }



}
