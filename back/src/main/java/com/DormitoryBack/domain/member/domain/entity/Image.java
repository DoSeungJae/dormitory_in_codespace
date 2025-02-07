package com.DormitoryBack.domain.member.domain.entity;

//presigned URL 방식으로 AWS S3에 이미지 파일 업로드 기능 구현 중. 그러므로 이 클래스는 삭제 예정
import javax.persistence.Lob;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
