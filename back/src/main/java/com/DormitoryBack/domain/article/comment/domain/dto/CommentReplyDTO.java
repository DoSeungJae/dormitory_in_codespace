package com.DormitoryBack.domain.article.comment.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentReplyDTO {
    private Long rootCommentId;
    private String content;

}
