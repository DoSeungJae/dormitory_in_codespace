package com.taxiWithBack.domain.article.api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("api/v1/article")
@Slf4j
public class ArticleApi {

    @GetMapping("")
    public ResponseEntity allArticles(){
        return ResponseEntity.status(HttpStatus.OK).body(null);
        //body는 commentlist를 반환
    }

    @GetMapping("/{dorId}")
    public ResponseEntity dorArticle(@PathVariable("dorId") Long dorId){
        return ResponseEntity.status(HttpStatus.OK).body(null);

    }

    @PostMapping("/{dorId}")
    public ResponseEntity newArticle(@PathVariable("dorId") Long dorId){
        return ResponseEntity.status(HttpStatus.OK).body(null);

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

