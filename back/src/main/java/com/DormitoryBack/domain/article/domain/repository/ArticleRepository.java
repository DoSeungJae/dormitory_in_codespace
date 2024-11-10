package com.DormitoryBack.domain.article.domain.repository;

import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.member.entity.User;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@EnableMongoRepositories 
public interface ArticleRepository extends MongoRepository<Article,Long> {

    Page<Article> findAllByDormId(Long dormId, Pageable pageable);
    Page<Article> findAllByUsrId(User user, Pageable pageable);

}
