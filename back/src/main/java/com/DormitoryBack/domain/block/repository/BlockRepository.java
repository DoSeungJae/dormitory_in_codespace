package com.DormitoryBack.domain.block.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DormitoryBack.domain.block.entity.Block;


public interface BlockRepository extends JpaRepository<Block,Long> {
    long countByBlockedUserIdAndBlockerId(Long blockedUserId, Long blockerId);

    List<Block> findAllByBlockerId(Long blockerId);


}
