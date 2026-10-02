package com.backend.notification.repository;

import com.backend.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByNotificationId(UUID notificationId);
    List<Notification> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
}