package com.DormitoryBack.domain.member.domain.dto;

import org.springframework.http.HttpHeaders;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageDTO {

    byte[] imageBytes;

    HttpHeaders headers;

}
