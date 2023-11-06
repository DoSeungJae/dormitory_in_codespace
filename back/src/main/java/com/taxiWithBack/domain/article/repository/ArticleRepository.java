package com.taxiWithBack.domain.article.repository;

import com.taxiWithBack.domain.article.entity.Article;
import com.taxiWithBack.domain.member.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    List<Article> findAllByDorId(Long dorId);

}
