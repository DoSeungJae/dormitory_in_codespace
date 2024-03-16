package com.DormitoryBack.domain.article.comment.domain.dto;

import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentPageResponseDTO {
    List<String> rootComments;
    List<String> replyComments;

}
