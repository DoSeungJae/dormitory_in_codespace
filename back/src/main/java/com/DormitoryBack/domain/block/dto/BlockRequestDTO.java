package com.DormitoryBack.domain.block.dto;

import javax.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class BlockRequestDTO {

    @NotNull
    private Long blockerId;

    @NotNull
    private Long blockedId;

}

