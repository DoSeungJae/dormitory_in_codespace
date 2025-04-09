package com.DormitoryBack.domain.article.comment.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DormitoryBack.domain.article.comment.domain.entity.OrphanComment;
import com.DormitoryBack.domain.article.domain.entity.Article;

public interface OrphanCommentRepository extends JpaRepository<OrphanComment,Long> {

    int countByArticle(Article article);
    
}
