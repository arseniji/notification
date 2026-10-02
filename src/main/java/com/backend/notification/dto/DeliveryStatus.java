package com.backend.notification.dto;

public enum DeliveryStatus {
    PENDING,      // создано, ещё не отправлено
    SENDING,      // в процессе отправки
    SENT,         // передано провайдеру
    DELIVERED,    // подтверждено доставкой (вебхук от провайдера)
    FAILED,       // окончательно упало
    RETRY,        // ждёт повторной попытки
    CANCELLED,    // отменено (например, отозвали уведомление)
    EXPIRED       // истекло окно отправки
}