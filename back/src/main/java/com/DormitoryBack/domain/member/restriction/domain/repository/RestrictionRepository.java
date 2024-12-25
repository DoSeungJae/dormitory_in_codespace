package com.DormitoryBack.domain.member.restriction.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.DormitoryBack.domain.member.restriction.domain.entity.Restriction;

public interface RestrictionRepository extends JpaRepository<Restriction,Long>{

    List<Restriction> findAllByUserId(Long userId);
    
    
}
