package com.DormitoryBack.domain.article.comment.domain.entity;

import com.DormitoryBack.domain.member.domain.entity.DeletedUser;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity(name="orphan_comment")
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class OrphanComment {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name="orphan_comment_id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name="deleted_user_id")
    private DeletedUser deletedUser;
   
}
