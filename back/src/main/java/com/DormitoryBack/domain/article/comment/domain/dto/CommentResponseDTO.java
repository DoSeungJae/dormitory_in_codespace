package com.DormitoryBack.domain.article.comment.domain.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponseDTO {
    List<String> rootComments;
    List<String> replyComments;
}
