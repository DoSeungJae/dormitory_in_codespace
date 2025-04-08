package com.DormitoryBack.domain.article.comment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DormitoryBack.domain.article.comment.domain.entity.OrphanComment;

public interface OrphanCommentRepository extends JpaRepository<OrphanComment,Long> {
    
}
