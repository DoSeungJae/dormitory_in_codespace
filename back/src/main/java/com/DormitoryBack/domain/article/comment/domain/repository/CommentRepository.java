package com.DormitoryBack.domain.article.comment.domain.repository;

import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import com.DormitoryBack.domain.member.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    //List<Comment> findAllByArticle(Article article);
    //articleId가 아니라
    //article로 해야함

    //Table의 column 이름이 아니라
    //Entity의 attribute 이름을 기준으로 Query가 실행되는 것으로 추측

    @Query(value = "SELECT DISTINCT(article_id) FROM comment WHERE user_id = :userId", nativeQuery = true)
    List<Long> findDistinctArticleIdsByUserId(@Param("userId") Long userId);

    List<Comment> findAllByUser(User user);
    List<Comment> findAllByArticleId(Long articleId);


    @Query(value = "SELECT * FROM comment c " +
                    "WHERE c.article_id = :articleId " +
                    "AND c.user_id NOT IN (:blockedIdList) " +
                    "AND (c.root_comment_id IS NULL OR " +
                    "(SELECT r.user_id FROM comment r WHERE r.id = c.root_comment_id) NOT IN (:blockedIdList))", nativeQuery = true)
    List<Comment> findByArticleIdExcludingBlockedComments(@Param("articleId") Long articleId, @Param("blockedIdList") List<Long> blockedIdList);
    //count 용도로 사용. 이 메서드를 통해 얻은 List<Comment>를 listStringify하면 serializer 관련 에러가 나타나기 때문에 실제 comment 필터링은 응용 계층에서 수행.
    //이 메서드는 blockedIdList의 size가 0일 때 제대로 작동하지 않음
    //왜냐하면 blockedIdList가 비어있다면 SQL에선 NULL로 처리되어 NOT IN NULL이 되는데, 이것은 항상 FALSE이기 때문. <<- 포스팅?

    
}
