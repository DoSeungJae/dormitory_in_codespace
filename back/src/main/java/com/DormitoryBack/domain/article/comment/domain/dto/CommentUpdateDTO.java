package com.DormitoryBack.domain.article.comment.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateDTO {
    private Long id;
    private String content;
    private Boolean isUpdated=true;
    //값을 정해도 되나?

}
