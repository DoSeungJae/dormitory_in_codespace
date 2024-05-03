package com.DormitoryBack.domain.group.domain.repository;


import com.DormitoryBack.domain.group.domain.entitiy.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group,Long> {
    List<Group> findAllByIsProceeding(Boolean isProceeding);
}
