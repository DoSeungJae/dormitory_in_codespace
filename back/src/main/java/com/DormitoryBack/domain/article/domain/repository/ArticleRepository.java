package com.DormitoryBack.domain.article.domain.repository;

import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.member.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    //List<Article> findAllByDorId(Long dorId);
    Page<Article> findAllByDorId(Long dorId, Pageable pageable);

    Page<Article> findAllByUsrId(User user, Pageable pageable);



}
