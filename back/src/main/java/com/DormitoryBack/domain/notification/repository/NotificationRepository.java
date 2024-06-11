package com.DormitoryBack.domain.notification.repository;

import com.DormitoryBack.domain.notification.entitiy.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findAllByIsConfirmed(Boolean isConfirmed);
}
