package com.DormitoryBack.domain.article.comment.domain.dto;

import java.time.LocalDateTime;

public class CommentResponseDTO {
    String content;
    private LocalDateTime time;
    private Long rootCommentId;
}
