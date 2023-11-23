package com.DormitoryBack.domain.article.repository;

import com.DormitoryBack.domain.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    List<Article> findAllByDorId(Long dorId);

}
