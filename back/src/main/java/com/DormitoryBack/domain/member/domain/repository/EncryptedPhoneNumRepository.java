package com.DormitoryBack.domain.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.DormitoryBack.domain.member.domain.entity.EncryptedPhoneNumMap;

public interface EncryptedPhoneNumRepository extends JpaRepository<EncryptedPhoneNumMap,Long>{

    EncryptedPhoneNumMap findByNumberHash(String numberHash);
}
