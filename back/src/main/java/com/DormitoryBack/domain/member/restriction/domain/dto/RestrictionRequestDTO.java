package com.DormitoryBack.domain.member.restriction.domain.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.DormitoryBack.domain.member.restriction.domain.enums.Function;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RestrictionRequestDTO {
    
    private String accessKey;
    private Long userId;
    private String reason;
    private Long durationDays;
    private List<String> suspendedFunctions;

    public List<Function> getSuspensionFunctionsAsEnum() {
        return suspendedFunctions.stream()
                                  .map(Function::fromString)
                                  .collect(Collectors.toList());
    }

    public void setAccessKey(String accessKey){
        this.accessKey=accessKey;
    }
    
    /*
     예상 json 예
    {
        "accessKey": "yourAccessKey",
        "userId":"4",
        "reason": "someReason",
        "suspensionDays": "10",
        "suspensionFunctions": ["LOGIN", "ARTICLE"]
    }

     */
}

