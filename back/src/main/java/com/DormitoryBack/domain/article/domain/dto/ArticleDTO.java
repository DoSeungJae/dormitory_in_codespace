package com.DormitoryBack.domain.article.domain.dto;

import java.time.LocalDateTime;

import com.DormitoryBack.domain.member.dto.UserResponseDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArticleDTO {

    Long id;

    String title;

    String contentHTML;

    Long dormId;

    String category;

    UserResponseDTO user;
    
    LocalDateTime createdTime;

}
