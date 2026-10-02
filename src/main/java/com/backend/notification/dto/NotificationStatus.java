package com.backend.notification.dto;

public enum NotificationStatus {
    CREATED,       // создано, ждёт обработки
    SCHEDULED,     // отложено
    PROCESSING,    // в работе
    SENT,          // передано провайдеру/в канал
    DELIVERED,     // подтверждено доставкой
    READ,          // прочитано (для in-app)
    FAILED,        // окончательно упало
    CANCELLED,     // отменено
    EXPIRED        // истекло
}
