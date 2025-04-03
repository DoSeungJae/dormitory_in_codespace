package com.DormitoryBack.domain.article.comment.domain.api;

import com.DormitoryBack.domain.article.comment.domain.dto.*;
import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import com.DormitoryBack.domain.article.comment.domain.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = {"https://improved-space-tribble-vjvwrwx956jh69w4-3000.app.github.dev", "https://turbo-chainsaw-rpvvwx9pp5c5p55-3000.app.github.dev"})
@RestController
@RequestMapping("api/v1/comment")
@Slf4j
public class CommentApi {
    
    @Autowired
    private CommentService commentService;

    @GetMapping("/test")
    public ResponseEntity<String> commentTest(){
        return ResponseEntity.status(HttpStatus.OK).body("comment test");
    }
    
    @GetMapping("/{commentId}")
    public ResponseEntity<String> Comment(@PathVariable("commentId") Long commentId){
        Comment comment=commentService.getComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(comment.toJsonString());
    }

    @GetMapping("")
    public ResponseEntity<List<String>> allComments() {
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity.status(HttpStatus.OK).body(commentService.listStringify(comments));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<String>> userComments(@PathVariable("userId") Long userId){
        List<Comment> comments=commentService.getUserComments(userId);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.listStringify(comments));
    }

    //토큰 인증이 필요하지 않을까?
    /*
    @GetMapping("/article/{articleId}")
    public ResponseEntity articleComments(@PathVariable("articleId") Long articleId){
        List<Comment> comments=commentService.getArticleComments(articleId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.listStringify(comments));
    }
    */
    
    @GetMapping("article/{articleId}")
    public ResponseEntity<CommentResponseDTO> articleCommentsPerPage(@PathVariable("articleId") Long articleId, @RequestHeader("Authorization") String token){
        CommentResponseDTO responseDTO=commentService.getArticleComments(articleId, token);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/numComments")
    public ResponseEntity<Long> numberOfComments(@RequestParam(name="articleId", defaultValue = "-1") Long articleId, @RequestHeader("Authorization") String token){
        Long number=commentService.getNumberOfComments(articleId,token);
        return ResponseEntity.status(HttpStatus.OK).body(number);
    }
    
    @PostMapping("/new")
    public ResponseEntity<String> newComment(@RequestBody CommentDTO dto, @RequestHeader("Authorization") String token){
        Comment comment=commentService.newComment(dto,token);
        return ResponseEntity.status(HttpStatus.OK).body(comment.toJsonString());
    }

    @PostMapping("/newReply")
    public ResponseEntity<CommentReplyResponseDTO> newReply(@RequestBody CommentReplyDTO dto, @RequestHeader("Authorization") String token){
        CommentReplyResponseDTO responseDTO=commentService.newReply(dto,token);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        //StrUtil을 사용하면 time이 Array 타입으로 반환됨,
        //dto 그대로 반환시 time은 string 타입으로 반환됨.

    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@RequestBody CommentUpdateDTO dto,@PathVariable("commentId") Long commentId, @RequestHeader("Authorization") String token){
        Comment updatedComment=commentService.updateComment(dto,commentId,token);
        return ResponseEntity.status(HttpStatus.OK).body(updatedComment.toJsonString());
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId, @RequestHeader("Authorization") String token){
        commentService.deleteComment(commentId,token);
        return ResponseEntity.status(HttpStatus.OK).body("CommentDeleted");
    }

}
