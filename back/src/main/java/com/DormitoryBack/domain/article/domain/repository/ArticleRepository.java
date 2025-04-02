package com.DormitoryBack.domain.article.domain.repository;

import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.member.domain.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories 
public interface ArticleRepository extends MongoRepository<Article,Long> {

    Page<Article> findAllByDormId(Long dormId, Pageable pageable);

    Page<Article> findAllByUsrId(User user, Pageable pageable); //이름에 Id가 포함돼있긴 하나, 사실상 user를 조회하는 메서드

    Page<Article> findAllByUserId(Long user, Pageable pageable);
    
    
    Page<Article> findByUserIdNotIn(List<Long> blockedIdList, Pageable pageable);

    Page<Article> findByDormIdAndUserIdNotIn(Long dormId, List<Long> blockedIdList, Pageable pageable);

    List<Article> findByIdAndUserIdNotIn(List<Long> idList, List<Long> blockedIdList);

}
