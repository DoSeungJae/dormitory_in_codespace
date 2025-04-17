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

    Page<Article> findAllByUsrId(User user, Pageable pageable); //이름에 Id가 포함돼있긴 하지만, 
    //사실상 User(user)로 조회하는 메서드. Article에 User타입 데이터 필드의 이름이 usrId 때문, 분명한 설계 미스.

    Page<Article> findAllByUserId(Long user, Pageable pageable); // Long 타입 파라미터 이름이 잘못됨. 고쳐야하는데 문제가 생길까봐 손 못대는중...
    
    
    Page<Article> findByUserIdNotIn(List<Long> blockedIdList, Pageable pageable);

    Page<Article> findByDormIdAndUserIdNotIn(Long dormId, List<Long> blockedIdList, Pageable pageable);

    //List<Article> findByIdAndUserIdNotIn(List<Long> idList, List<Long> blockedIdList); 
    //이거는 안됨. 단일 id로 조회하는 메서드이기 때문. 그리고 그 "id"라는 걸 List<Long> 타입으로 받아버리기 때문에 
    //모든 상황에서 빈 리스트를 반환할 것임. 

    List<Article> findByIdInAndUserIdNotIn(List<Long> idList, List<Long> blockedIdList);
    //copliot이 제안한 메서드... 이건 의도대로 작동함. 새삼 놀랍다.

}
