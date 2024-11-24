package com.DormitoryBack.domain.article.comment.domain.repository;

import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import com.DormitoryBack.domain.article.domain.entity.Article;
import com.DormitoryBack.domain.member.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    //List<Comment> findAllByArticle(Article article);
    //articleId가 아니라
    //article로 해야함

    //Table의 attribute의 이름이 아니라
    //Entity의 attribute의 이름을 기준으로 Query가 실행되는 것으로 추측

    @Query(value = "SELECT DISTINCT(article_id) FROM comment WHERE user_id = :userId", nativeQuery = true)
    List<Long> findDistinctArticleIdsByUserId(@Param("userId") Long userId);

    List<Comment> findAllByUser(User user);
    List<Comment> findAllByArticleId(Long articleId);

}
