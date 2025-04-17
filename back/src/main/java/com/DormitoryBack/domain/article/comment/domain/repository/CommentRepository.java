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


    /*
    @Query(value = "SELECT * FROM comment c " +
                    "WHERE c.article_id = :articleId " +
                    "AND c.user_id NOT IN (:blockedIdList) " +
                    "AND (c.root_comment_id IS NULL OR " +
                    "(SELECT r.user_id FROM comment r WHERE r.id = c.root_comment_id) NOT IN (:blockedIdList))", nativeQuery = true)
    List<Comment> findByArticleIdExcludingBlockedComments(@Param("articleId") Long articleId, @Param("blockedIdList") List<Long> blockedIdList);
     */
    
    //이 메서드를 통해 얻은 List<Comment>를 listStringify하면 serializer 관련 에러가 나타나기 때문에 실제 comment 필터링은 응용 계층에서 수행. 그러나 원하는 comment는 잘 조회하므로 카운트 용도로 사용.

    //이 메서드는 blockedIdList의 size가 0일 때 제대로 작동하지 않음
    //왜냐하면 blockedIdList가 비어있다면 SQL에선 NULL로 처리되어(추측) NOT IN NULL이 되는데, 이 경우는 항상 FALSE이기 때문. <<- 포스팅?    


    //25.04.17
    //사실 카운트도 잘 안됨, 탈퇴한 사용자의 게시물을 차단하면 (차단되지 않은) 다른 모든 사용자의 게시물의 articlePreview에서 댓글 카운트가 0이 됨.
    //물론 articlePage에서는 댓글이 잘 조회되기는함.(정 안되면 articlePage에서 댓글을 조회하는 방식을 사용해 count를 구하는 방법도 있음. 근데 효율이 떨어지겠죠?)
    //edge case: blockedIdList가 비어있지 않을 때.
    //사용자가 탈퇴한 게시글은 user_id가 null이 됨....
    //OrphanCommentRepository에 query를 날려 보면 되지 않을까? <- 이 처리도 쉽지 않음.

    //findByArticleIdExcludingBlockedComments메서드 사용하지 않기로 결정.

}
