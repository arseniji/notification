package com.backend.notification.repository;

import com.backend.notification.dto.NotificationChannel;
import com.backend.notification.model.MessageTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long> {
    Optional<MessageTemplate> findByEventCodeAndChannelAndLocaleAndActiveTrue(
            String eventCode, NotificationChannel channel, String locale);
}
