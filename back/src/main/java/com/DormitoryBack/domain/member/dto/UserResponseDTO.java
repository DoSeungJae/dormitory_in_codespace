package com.DormitoryBack.domain.member.dto;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    private String eMail;
    private String nickName;

}
