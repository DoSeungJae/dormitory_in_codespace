package com.DormitoryBack.domain.article.domain.service;

import com.DormitoryBack.domain.article.comment.domain.repository.CommentRepository;
import com.DormitoryBack.domain.article.domain.dto.ArticleDTO;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.article.domain.repository.ArticleRepository;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.member.entity.User;
import com.DormitoryBack.domain.member.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleService {
    @Autowired
    private CommentRepository commentRepository;
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

    public Page<Article> getAllArticlesWithinPage(int page, int size){
        Pageable pageable= PageRequest.of(page,size, Sort.by("createTime").descending());
        Page<Article> articlePage=articleRepository.findAll(pageable);
        if(articlePage.isEmpty() && page==0){
            throw new RuntimeException("ArticleNotFound");
        }
        else if(articlePage.isEmpty()){
            throw new RuntimeException("NoMoreArticlePage");
        }
        return articlePage;
    }

    public Page<Article> getAllArticlesWithinRangePage(int startPage, int endPage, int size){
        List<Article> concatenatedPagesAsList=new ArrayList<>();
        for(int page=startPage;page<endPage+1;page++){
            Pageable pageable=PageRequest.of(page,size,Sort.by("createTime").descending());
            Page<Article> articlePage=articleRepository.findAll(pageable);
            if(articlePage.isEmpty()){
                throw new RuntimeException("ExceededPage");
            }
            concatenatedPagesAsList.addAll(articlePage.getContent());
        }
        Page<Article> articleRangePage=new PageImpl<>(
                concatenatedPagesAsList,
                PageRequest.of(0,concatenatedPagesAsList.size()),
                concatenatedPagesAsList.size()
        );

        return articleRangePage;
    }

    /*
    public List<Article> getDorArticles(Long dorId){
        List<Article> dorArticles=articleRepository.findAllByDorId(dorId);
        if(dorArticles.isEmpty()){
            throw new RuntimeException("유효하지 않는 기숙사 번호이거나 글이 존재하지 않습니다.");
        }
        return dorArticles;
    }

     */

    public Page<Article> getDorArticlesPerPage(Long dorId,int page,int size){
        Pageable pageable=PageRequest.of(page,size,Sort.by("createTime").descending());
        Page<Article> articlePage=articleRepository.findAllByDorId(dorId,pageable);
        if(articlePage.isEmpty() && page==0){
            throw new RuntimeException("ArticleNotFound");
        }
        else if(articlePage.isEmpty()){
            throw new RuntimeException("NoMoreArticlePage");
        }
        return articlePage;
    }

    public Page<Article> getDorArticlesWithinRangePage(Long dorId, int startPage, int endPage, int size){
        List<Article> concatenatedPagesAsList=new ArrayList<>();
        for(int page=startPage;page<endPage+1;page++){
            Pageable pageable=PageRequest.of(page,size,Sort.by("createTime").descending());
            Page<Article> articlePage=articleRepository.findAllByDorId(dorId,pageable);
            if(articlePage.isEmpty() && page==0){
                throw new RuntimeException("ArticleNotFound");
            }
            else if(articlePage.isEmpty()){
                throw new RuntimeException("ExceededPage");
            }
            concatenatedPagesAsList.addAll(articlePage.getContent());
        }
        Page<Article> rangePage=new PageImpl<>(
                concatenatedPagesAsList,
                PageRequest.of(0,concatenatedPagesAsList.size()),
                concatenatedPagesAsList.size()
        );
        return rangePage;
    }

    public Page<Article> getArticlesByUser(int page, int size, String token) {
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("유효하지 않은 토큰입니다.");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        User user=userRepository.findById(userId).orElse(null);
        Pageable pageable=PageRequest.of(page,size,Sort.by("createTime").descending());
        Page<Article> userArticles=articleRepository.findAllByUsrId(user,pageable);
        if(userArticles.isEmpty() && page==0){
            throw new RuntimeException("ArticleNotFound");
        }
        else if(userArticles.isEmpty()){
            throw new RuntimeException("ExceededPage");
        }
        return userArticles;
    }

    public Page<Article> getArticlesCommentedFromUser(int page, int size, String token) {
        if(!tokenProvider.validateToken(token)){
            throw new JwtException("유효하지 않은 토큰입니다.");
        }
        Long userId=tokenProvider.getUserIdFromToken(token);
        User user=userRepository.findById(userId).orElse(null);
        Pageable pageable=PageRequest.of(page,size,Sort.by("createTime").descending());
        Page<Article> commentedUserArticles=articleRepository.
                findAllArticlesByCommentedUsrId(userId,pageable);
        if(commentedUserArticles.isEmpty() && page==0){
            throw new RuntimeException("ArticleNotFound");
        }
        else if(commentedUserArticles.isEmpty()){
            throw new RuntimeException("ExceededPage");
        }
        return commentedUserArticles;

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
    public List<String> pageStringify(Page<Article> articlePage){
        List<String> stringifiedArticleList=articlePage.getContent()
                .stream()
                .map(Article::toJsonString)
                .collect(Collectors.toList());

        return stringifiedArticleList;
    }



}
