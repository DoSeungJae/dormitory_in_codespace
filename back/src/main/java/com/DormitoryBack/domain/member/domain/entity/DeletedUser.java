package com.DormitoryBack.domain.member.domain.entity;

import com.DormitoryBack.domain.auth.oauth.domain.enums.ProviderType;
import com.DormitoryBack.domain.member.domain.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Entity(name="deleted_user")
@Builder
@Getter
public class DeletedUser { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique = false, name="email")
    private String encryptedEmail;

    @Column(nullable = false)
    @JsonIgnore
    private String passWord;

    @Column
    private String nickName;

    @Column
    private Long dormId;

    @Column
    @Enumerated(EnumType.STRING)
    private ProviderType provider;

    @Column
    private String imageName;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleType role;

}
