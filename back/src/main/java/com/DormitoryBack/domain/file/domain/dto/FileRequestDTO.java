package com.DormitoryBack.domain.file.domain.dto;

import com.DormitoryBack.domain.file.domain.enums.ParamType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileRequestDTO<T> {
    
    private ParamType type; //NICKNAME or USERID

    private T userInfo; 

}
