package com.DormitoryBack.domain.article.api;
import com.DormitoryBack.domain.article.dto.ArticleDTO;
import com.DormitoryBack.domain.article.entity.Article;
import com.DormitoryBack.domain.article.service.ArticleService;
import com.DormitoryBack.domain.jwt.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("api/v1/article")
@Slf4j
public class ArticleApi {
    private final ArticleService articleService;
    private final TokenProvider tokenProvider;
    @Autowired
    public ArticleApi(ArticleService articleService, TokenProvider tokenProvider){
        this.tokenProvider=tokenProvider;
        this.articleService=articleService;
    }
    @GetMapping("/test")
    public String articleTest(){
        String msg="article test";
        log.info(msg);
        return msg;
    }
    @GetMapping("")
    public ResponseEntity allArticles(){
        List<Article> articles=articleService.getAllArticles();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(articleService.listStringify(articles));
    }

    @GetMapping("/dor/{dorId}")
    public ResponseEntity dorArticles(@PathVariable("dorId") Long dorId){
        List<Article> articles=articleService.getDorArticles(dorId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(articleService.listStringify(articles));
    }

    @GetMapping("{articleId}")
    public ResponseEntity article(@PathVariable("articleId") Long articleId){ //token 인증이 필요함.
        Article article=articleService.getArticle(articleId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(article);
    }

    @PostMapping("/new")
    public ResponseEntity newArticle(@RequestBody ArticleDTO dto, @RequestHeader("Authorization") String token){
        Article article=articleService.newArticle(dto,token);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(article.toString());
    }

    @PatchMapping("/{articleId}")
    public ResponseEntity updateArticle(@PathVariable("articleId") Long articleId, @RequestBody ArticleDTO dto,@RequestHeader("Authorization") String token) {
        Article updatedArticle=articleService.updateArticle(dto,articleId,token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedArticle);
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity deleteArticle(@PathVariable("articleId") Long articleId, @RequestHeader("Authorization") String token){
        articleService.deleteArticle(articleId,token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("ARTICLE DELETED");
    }
    @GetMapping("/validate") //글쓰기 페이지에 입장하기전 토큰 유효성 검사
    public ResponseEntity tokenValidation(@RequestHeader("Authorization") String token){
        Boolean isValid=tokenProvider.validateToken(token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(isValid);
    }

}

