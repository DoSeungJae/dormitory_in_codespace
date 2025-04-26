package com.DormitoryBack.domain.article.comment.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {
    Long number;
    List<String> rootComments;
    List<String> replyComments;
}
