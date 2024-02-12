package com.DormitoryBack.domain.article.comment.domain.repository;

import com.DormitoryBack.domain.article.comment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

}
