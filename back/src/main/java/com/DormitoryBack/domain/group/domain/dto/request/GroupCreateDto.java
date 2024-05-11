
package com.DormitoryBack.domain.group.domain.dto.request;

import com.DormitoryBack.domain.article.domain.entity.Article;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GroupCreateDto {

    @NotNull
    private Long articleId;
    private Long maxCapacity;


}
