package com.DormitoryBack.domain.group.domain.dto.response;

import java.time.LocalDateTime;

public class GroupCreateDto {
    private Long id; //response dto에 꼭 그룹 id가 포함되어야할까?
    private Long dormId;
    private Long hostId;
    private Long articleId;
    private String category;

}
