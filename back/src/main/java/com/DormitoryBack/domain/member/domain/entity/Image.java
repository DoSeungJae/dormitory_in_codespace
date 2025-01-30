package com.DormitoryBack.domain.member.domain.entity;

import javax.persistence.Lob;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, name="user_id")
    private Long userId;

    @Lob
    @Column(nullable=false, unique=false, name="image_bytes")
    private byte[] imageBytes;

    @Column(nullable=false, unique=false, name="content_type")
    private String contentType;

    @Column(nullable=false, unique=false, name="file_name")
    private String fileName;



    

}
