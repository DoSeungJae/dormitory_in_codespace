package com.DormitoryBack.domain.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DormitoryBack.domain.member.domain.entity.DeletedUser;

public interface DeletedUserRepository extends JpaRepository<DeletedUser,Long>{
    
}
