package com.DormitoryBack.domain.article.domain.api;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.jwt.TokenProvider;
import com.DormitoryBack.domain.article.domain.dto.NewArticleDTO;
import com.DormitoryBack.domain.article.domain.dto.ArticleDTO;
import com.DormitoryBack.domain.article.domain.dto.ArticlePreviewDTO;
import com.DormitoryBack.domain.article.domain.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"https://improved-space-tribble-vjvwrwx956jh69w4-3000.app.github.dev", "https://turbo-chainsaw-rpvvwx9pp5c5p55-3000.app.github.dev"})
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
    public ResponseEntity<List<ArticlePreviewDTO>> allArticles(@RequestHeader("Authorization") String token, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
        List<ArticlePreviewDTO> articlesDTO=articleService.getAllArticlesWithinPage(page,size,token);
        return ResponseEntity.status(HttpStatus.OK).body(articlesDTO);
    }

    @GetMapping("/filter/user")
    public ResponseEntity<List<ArticlePreviewDTO>> userArticles(@RequestParam(defaultValue = "0")int page, @RequestParam(defaultValue = "10")int size, @RequestHeader("Authorization") String token){
        List<ArticlePreviewDTO> articlesDTO=articleService.getUserArticlesWithinPage(page,size,token);
        return ResponseEntity.status(HttpStatus.OK).body(articlesDTO);
    }

    @GetMapping("/filter/userCommented")
    public ResponseEntity<List<ArticlePreviewDTO>> userCommentedArticles(@RequestParam(defaultValue = "0")int page,@RequestParam(defaultValue = "10")int size,@RequestHeader("Authorization") String token){
        List<ArticlePreviewDTO> articlesDTO=articleService.getUserCommentedArticlesWithinPage(page,size,token);
        return ResponseEntity.status(HttpStatus.OK).body(articlesDTO);
    }
  
    @GetMapping("/dorm/{dormId}")
    public ResponseEntity<List<ArticlePreviewDTO>> DormArticles(@RequestHeader("Authorization") String token, @PathVariable("dormId") Long dormId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10")int size){
        List<ArticlePreviewDTO> articlesDTO=articleService.getDormArticlesWithinPage(dormId,page,size,token);
        return ResponseEntity.status(HttpStatus.OK).body(articlesDTO);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleDTO> article(@PathVariable("articleId") Long articleId){
        ArticleDTO articleDTO=articleService.getArticle(articleId);
        return ResponseEntity.status(HttpStatus.OK).body(articleDTO);
    }
    
    @GetMapping("/isArticle/{articleId}")
    public ResponseEntity<Boolean> isArticle(@PathVariable("articleId") Long articleId){
        Boolean isArticle=articleService.checkArticleExist(articleId);
        return ResponseEntity.status(HttpStatus.OK).body(isArticle);
    }

    @GetMapping("/writerNickName/{articleId}")
    public ResponseEntity<String> writerNickName(@PathVariable("articleId") Long articleId){
        String userNickName=articleService.getWriterNickName(articleId);
        return ResponseEntity.status(HttpStatus.OK).body(userNickName);
    }
    
    @GetMapping("/validate") 
    public ResponseEntity<Boolean> tokenValidation(@RequestHeader("Authorization") String token){
        Boolean isValid=tokenProvider.validateToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(isValid);
    }

    @PostMapping("/new")
    public ResponseEntity<String> newArticle(@RequestBody NewArticleDTO dto, @RequestHeader("Authorization") String token){
        Article article=articleService.saveNewArticle(dto,token);
        return ResponseEntity.status(HttpStatus.CREATED).body(article.toJsonString());
    }

    @PatchMapping("/{articleId}")
    public ResponseEntity<String> updateArticle(@PathVariable("articleId") Long articleId, @RequestBody NewArticleDTO dto,@RequestHeader("Authorization") String token) {
        Article updatedArticle=articleService.updateArticle(dto,articleId,token);
        return ResponseEntity.status(HttpStatus.OK).body(updatedArticle.toJsonString());
    }

    @DeleteMapping("/{articleId}")
    public ResponseEntity<String> deleteArticle(@PathVariable("articleId") Long articleId, @RequestHeader("Authorization") String token){
        articleService.deleteArticle(articleId,token);
        return ResponseEntity.status(HttpStatus.OK).body("ARTICLE DELETED");
    }

}

