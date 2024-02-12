package com.DormitoryBack.domain.article.comment.domain.dto;

import com.DormitoryBack.domain.article.domain.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private Long id;
    private Long articleId;
    private String content;
    private LocalDateTime createdTime;



}
