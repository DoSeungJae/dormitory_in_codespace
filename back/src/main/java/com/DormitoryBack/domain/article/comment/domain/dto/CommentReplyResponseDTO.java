package com.DormitoryBack.domain.article.comment.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CommentResponseDTO {
    String content;
    private LocalDateTime time;
    private Long rootCommentId;
}
