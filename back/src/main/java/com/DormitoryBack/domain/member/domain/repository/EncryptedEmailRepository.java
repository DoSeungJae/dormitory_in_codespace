package com.DormitoryBack.domain.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DormitoryBack.domain.member.domain.entity.EncryptedEmailMap;

public interface EncryptedEmailRepository extends JpaRepository<EncryptedEmailMap,Long> {

    EncryptedEmailMap findByEmailHash(String emailHash);
    
}
