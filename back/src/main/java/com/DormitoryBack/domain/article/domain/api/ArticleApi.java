package com.DormitoryBack.domain.article.domain.api;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.article.domain.dto.ArticleDTO;
import com.DormitoryBack.domain.article.domain.service.ArticleService;
import com.DormitoryBack.global.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity allArticles(@RequestParam(defaultValue="0") int page,
                                      @RequestParam(defaultValue="10") int size){
        //List<Article> articles=articleService.getAllArticles();
        Page<Article> articles=articleService.getAllArticlesWithinPage(page,size);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(articleService.pageStringify(articles));
    }

    @GetMapping("/filter/user")
    public ResponseEntity articlesByUser(@RequestParam(defaultValue = "0")int page,
                                         @RequestParam(defaultValue = "10")int size,
                                         @RequestHeader("Authorization") String token){
        Page<Article> articles=articleService.getArticlesByUser(page,size,token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(articleService.pageStringify(articles));

    }

    @GetMapping("/filter/userComment")
    public ResponseEntity articlesCommentedFromUser(@RequestParam(defaultValue = "0")int page,
                                                    @RequestParam(defaultValue = "100")int size,
                                                    @RequestHeader("Authorization") String token){

        Page<Article> articles=articleService.getArticlesCommentedFromUser(page,size,token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(articleService.pageStringify(articles));
    }


    @GetMapping("/range")
    public ResponseEntity allArticleRangePage
            (@RequestParam(defaultValue="0") int start,
             @RequestParam(defaultValue="0") int end,
             @RequestParam(defaultValue = "10") int size){

        Page<Article> articleRangePage=articleService
                .getAllArticlesWithinRangePage(start,end,size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(articleService.pageStringify(articleRangePage));
    }

    /*
    @GetMapping("/dor/{dorId}")
    public ResponseEntity dorArticles(@PathVariable("dorId") Long dorId){
        List<Article> articles=articleService.getDorArticles(dorId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(articleService.listStringify(articles));
    }
     */
    @GetMapping("/dor/{dorId}")
    public ResponseEntity dorArticlesPerPage(
            @PathVariable("dorId") Long dorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10")int size){

        Page<Article> dorArticlePage=articleService
                .getDorArticlesPerPage(dorId,page,size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(articleService.pageStringify(dorArticlePage));

    }
    @GetMapping("/dor/{dorId}/range")
    public ResponseEntity dorArticleRangePage(
            @PathVariable("dorId") Long dorId,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "0") int end,
            @RequestParam(defaultValue = "10") int size){

        Page<Article> dorArticleRangePage=articleService.
                getDorArticlesWithinRangePage(dorId,start,end,size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(articleService.pageStringify(dorArticleRangePage));

    }
    @GetMapping("/isArticle/{articleId}")
    public ResponseEntity isArticle(@PathVariable("articleId") Long articleId){
        Boolean isArticle=articleService.checkArticleExist(articleId);
        return ResponseEntity.status(HttpStatus.OK).body(isArticle);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity article(@PathVariable("articleId") Long articleId){
        Article article=articleService.getArticle(articleId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(article.toJsonString());
    }

    @PostMapping("/new")
    public ResponseEntity newArticle(@RequestBody ArticleDTO dto, @RequestHeader("Authorization") String token){
        Article article=articleService.newArticle(dto,token);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(article.toJsonString());
    }

    @PatchMapping("/{articleId}")
    public ResponseEntity updateArticle(@PathVariable("articleId") Long articleId, @RequestBody ArticleDTO dto,@RequestHeader("Authorization") String token) {
        Article updatedArticle=articleService.updateArticle(dto,articleId,token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedArticle.toJsonString());
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

