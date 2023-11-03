package com.taxiWithBack.domain.article.api;
import com.taxiWithBack.domain.article.dto.ArticleDTO;
import com.taxiWithBack.domain.article.entity.Article;
import com.taxiWithBack.domain.article.service.ArticleService;
import com.taxiWithBack.domain.member.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("api/v1/article")
@Slf4j
public class ArticleApi {

    private final ArticleService articleService;

    @Autowired
    public ArticleApi(ArticleService articleService){
        this.articleService=articleService;
    }


    @GetMapping("/Test")
    public String articleTest(){
        String msg="article test";
        log.info(msg);
        return msg;

    }

    @GetMapping("")
    public ResponseEntity allArticles(){
        return ResponseEntity.status(HttpStatus.OK).body(null);
        //body는 commentlist를 반환
    }

    @GetMapping("/{dorId}")
    public ResponseEntity dorArticle(@PathVariable("dorId") Long dorId){

        return ResponseEntity.status(HttpStatus.OK).body(null);

    }

    @PostMapping("/new")
    public ResponseEntity newArticle(@RequestBody ArticleDTO dto){
        Article article=articleService.newArticle(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(article.toString());


    }

    @PatchMapping("/{dorId}")
    public ResponseEntity patchArticle(@PathVariable("dorId") Long dorId){
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping("/{dorId}")
    public ResponseEntity deleteArticle(@PathVariable("dorId") Long dorId){
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }






}

