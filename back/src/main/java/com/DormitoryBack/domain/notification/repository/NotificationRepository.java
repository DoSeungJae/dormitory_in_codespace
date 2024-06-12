package com.DormitoryBack.domain.notification.repository;

import com.DormitoryBack.domain.notification.entitiy.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface NotificationRepository extends JpaRepository<Notification,Long> {
    List<Notification> findAllByIsConfirmed(Boolean isConfirmed);

    @Query(value = "SELECT * FROM NOTIFICATION WHERE is_confirmed=0 AND is_valid=1",nativeQuery = true)
    List<Notification> findAliveNotifications();
        //isConfirmed : 0 && //isValid : 1인 Notifications
}
