package com.DormitoryBack.domain.article.domain.repository;

import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.member.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@EnableMongoRepositories 
public interface ArticleRepository extends MongoRepository<Article,Long> {
    //List<Article> findAllByDorId(Long dorId);
    Page<Article> findAllByDormId(Long dormId, Pageable pageable);
    Page<Article> findAllByUsrId(User user, Pageable pageable);


    //@Query("SELECT a FROM Article a JOIN a.comments c WHERE c.user.id = :usrId")
    //Page<Article> findAllArticlesByCommentedUsrId(@Param("usrId") Long userId, Pageable pageable);


}
