package com.DormitoryBack.domain.article.domain.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArticlePreviewDTO {

    Long id;

    String title;

    String contentText;

    String userNickName;

    Long dormId;

    Long numComments;

    Long groupNumMembers;

    Long groupMaxCapacity;

    LocalDateTime createdTime;

}
