package com.DormitoryBack.domain.article.comment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long articleId;
    private String content;

}
