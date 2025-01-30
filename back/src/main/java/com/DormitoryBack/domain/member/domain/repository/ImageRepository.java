package com.DormitoryBack.domain.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DormitoryBack.domain.member.domain.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Image findByUserId(Long userId);
    
}
