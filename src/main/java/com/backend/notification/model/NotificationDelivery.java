package com.backend.notification.model;

import jakarta.persistence.*;
import com.backend.notification.dto.DeliveryStatus;
import com.backend.notification.dto.NotificationChannel;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "notification_deliveries",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_delivery_notification_channel",
                columnNames = {"notification_id", "channel"}
        ),
        indexes = {
                @Index(name = "idx_delivery_status_retry", columnList = "status, next_retry_at"),
                @Index(name = "idx_delivery_notification", columnList = "notification_id"),
                @Index(name = "idx_delivery_provider_msg", columnList = "provider_message_id")
        }
)
public class NotificationDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * UUID уведомления (Notification.notificationId).
     * Связь по бизнес-ключу, не по Long id — стабильнее между сервисами.
     */
    @Column(name = "notification_id", nullable = false, updatable = false)
    private UUID notificationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 32, updatable = false)
    private NotificationChannel channel;

    /**
     * Статус доставки. Отдельно от Notification.status.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private DeliveryStatus status = DeliveryStatus.PENDING;

    /**
     * Куда шлём: email, телефон, device token, webhook URL.
     * Хранится замаскированным или шифрованным — это PII.
     */
    @Column(name = "recipient", length = 255)
    private String recipient;

    /**
     * Имя провайдера: SMTP, TWILIO, FCM, APNS, TELEGRAM, INTERNAL.
     */
    @Column(name = "provider", length = 64)
    private String provider;

    /**
     * ID сообщения у провайдера — для reconciliation и вебхуков о статусе.
     */
    @Column(name = "provider_message_id", length = 128)
    private String providerMessageId;

    @Column(name = "attempt_count", nullable = false)
    private int attemptCount = 0;

    @Column(name = "last_attempt_at")
    private Instant lastAttemptAt;

    @Column(name = "next_retry_at")
    private Instant nextRetryAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "delivered_at")
    private Instant deliveredAt;

    @Column(name = "failed_at")
    private Instant failedAt;

    /**
     * Код ошибки от провайдера или внутренний.
     * Короткий, machine-readable: SMTP_550, FCM_INVALID_TOKEN, TIMEOUT.
     */
    @Column(name = "error_code", length = 64)
    private String errorCode;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Version
    @Column(name = "version_lock", nullable = false)
    private Long versionLock;

    public NotificationDelivery() {}

    @PrePersist
    void prePersist() {
        Instant now = Instant.now();
        if (createdAt == null) createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = Instant.now();
    }

    // === getters / setters ===

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getNotificationId() { return notificationId; }
    public void setNotificationId(UUID notificationId) { this.notificationId = notificationId; }

    public NotificationChannel getChannel() { return channel; }
    public void setChannel(NotificationChannel channel) { this.channel = channel; }

    public DeliveryStatus getStatus() { return status; }
    public void setStatus(DeliveryStatus status) { this.status = status; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getProviderMessageId() { return providerMessageId; }
    public void setProviderMessageId(String providerMessageId) { this.providerMessageId = providerMessageId; }

    public int getAttemptCount() { return attemptCount; }
    public void setAttemptCount(int attemptCount) { this.attemptCount = attemptCount; }

    public Instant getLastAttemptAt() { return lastAttemptAt; }
    public void setLastAttemptAt(Instant lastAttemptAt) { this.lastAttemptAt = lastAttemptAt; }

    public Instant getNextRetryAt() { return nextRetryAt; }
    public void setNextRetryAt(Instant nextRetryAt) { this.nextRetryAt = nextRetryAt; }

    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }

    public Instant getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(Instant deliveredAt) { this.deliveredAt = deliveredAt; }

    public Instant getFailedAt() { return failedAt; }
    public void setFailedAt(Instant failedAt) { this.failedAt = failedAt; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Long getVersionLock() { return versionLock; }
    public void setVersionLock(Long versionLock) { this.versionLock = versionLock; }
}