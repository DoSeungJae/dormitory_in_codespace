package com.DormitoryBack.domain.article.comment.domain.api;

import com.DormitoryBack.domain.article.comment.domain.dto.*;
import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import com.DormitoryBack.domain.article.comment.domain.service.CommentService;
import com.DormitoryBack.domain.jwt.TokenProvider;
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
@RequestMapping("api/v1/comment")
@Slf4j
public class CommentApi {
    private final CommentService commentService;
    private final TokenProvider tokenProvider;

    @Autowired
    public CommentApi(CommentService commentService,TokenProvider tokenProvider){
        this.tokenProvider=tokenProvider;
        this.commentService=commentService;
    }

    @GetMapping("/test")
    public ResponseEntity commentTest(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("comment test");
    }
    @GetMapping("/{commentId}")
    public ResponseEntity Comment(@PathVariable("commentId") Long commentId){
        Comment comment=commentService.getComment(commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(comment.toJsonString());
    }
    @GetMapping("")
    public ResponseEntity allComments() {
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.listStringify(comments));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity userComments(@PathVariable("userId") Long userId){
        List<Comment> comments=commentService.getUserComments(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commentService.listStringify(comments));
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
    public ResponseEntity articleCommentsPerPage(
            @PathVariable("articleId") Long articleId
    ){
        CommentResponseDTO responseDTO=commentService
                .getArticleComments(articleId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseDTO);
    }
    @PostMapping("/new")
    public ResponseEntity newComment(@RequestBody CommentDTO dto, @RequestHeader("Authorization") String token){
        Comment comment=commentService.newComment(dto,token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(comment.toJsonString());
    }
    @PostMapping("/newReply")
    public ResponseEntity newReply(@RequestBody CommentReplyDTO dto, @RequestHeader("Authorization") String token){
        CommentReplyResponseDTO responseDTO=commentService.newReply(dto,token);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseDTO);
        //StrUtil을 사용하면 time이 Array 타입으로 반환됨,
        //dto 그대로 반환시 time은 string 타입으로 반환됨.

    }
    @PatchMapping("/{commentId}")
    public ResponseEntity updateComment(@RequestBody CommentUpdateDTO dto,@PathVariable("commentId") Long commentId, @RequestHeader("Authorization") String token){
        Comment updatedComment=commentService.updateComment(dto,commentId,token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedComment.toJsonString());
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity deleteComment(@PathVariable("commentId") Long commentId, @RequestHeader("Authorization") String token){
        commentService.deleteComment(commentId,token);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("CommentDeleted");
    }

}
