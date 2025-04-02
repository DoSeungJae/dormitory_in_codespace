package com.DormitoryBack.domain.block.entity;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name="block")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Block {

    @Id
    @GeneratedValue
    @Column(name="id", nullable = false)
    private Long id;

    @Column(name="blocked_user_id", nullable = false)
    private Long blockedUserId;

    @Column(name="blocker_id", nullable = false)
    private Long blockerId;

    @Column(name="blocked_time", nullable = false)
    private LocalDateTime blockedTime;

}
