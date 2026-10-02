package com.backend.notification.model;

import jakarta.persistence.*;
import com.backend.notification.dto.NotificationChannel;
import java.time.Instant;

@Entity
@Table(
        name = "message_templates",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_template_event_channel_locale_bank",
                columnNames = {"event_code", "channel", "locale", "bank_id"}
        ),
        indexes = {
                @Index(name = "idx_template_event_channel", columnList = "event_code, channel"),
                @Index(name = "idx_template_active", columnList = "active")
        }
)
public class MessageTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Код события, например CARD_BLOCKED, PAYMENT_COMPLETED.
     * Строка, а не enum — чтобы команды могли добавлять события без релиза shared-модуля.
     */
    @Column(name = "event_code", nullable = false, length = 100)
    private String eventCode;

    /**
     * Канал доставки: IN_APP, EMAIL, SMS, PUSH, WEBHOOK.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "channel", nullable = false, length = 32)
    private NotificationChannel channel;

    /**
     * Локаль: ru-RU, kk-KZ, en-US.
     */
    @Column(name = "locale", nullable = false, length = 10)
    private String locale = "ru-RU";

    /**
     * Банк/тенант. null — общий шаблон для всех банков.
     */
    @Column(name = "bank_id")
    private Long bankId;

    /**
     * Тема — для EMAIL, PUSH, WEBHOOK.
     */
    @Column(name = "subject", length = 255)
    private String subject;

    /**
     * Основной шаблон. Плейсхолдеры вида {{amount}}, {{cardMask}}.
     * Не кладём сюда PII/PCI в открытом виде.
     */
    @Column(name = "body", nullable = false, length = 10000)
    private String body;

    /**
     * Шаблон для группировки (дайджест). null — не группируется.
     * Например, для N платежей в один push.
     */
    @Column(name = "grouped_body", length = 10000)
    private String groupedBody;

    /**
     * Версия шаблона. Апрувнутая версия — та, что реально используется.
     */
    @Column(name = "version", nullable = false)
    private int version = 1;

    /**
     * Активен ли шаблон. Старые версии помечаем false, не удаляем — аудит.
     */
    @Column(name = "active", nullable = false)
    private boolean active = true;

    /**
     * Обязательный ли канал для клиента (regulatory).
     * Например, блокировка карты, OTP, подозрительная активность.
     * Такие нельзя отключить в NotificationPreference.
     */
    @Column(name = "mandatory", nullable = false)
    private boolean mandatory = false;

    /**
     * Аудит апрува. В банке шаблоны должны проходить ревью.
     */
    @Column(name = "approved_by", length = 64)
    private String approvedBy;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Version
    @Column(name = "version_lock", nullable = false)
    private Long versionLock;

    public MessageTemplate() {}

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

    public String getEventCode() { return eventCode; }
    public void setEventCode(String eventCode) { this.eventCode = eventCode; }

    public NotificationChannel getChannel() { return channel; }
    public void setChannel(NotificationChannel channel) { this.channel = channel; }

    public String getLocale() { return locale; }
    public void setLocale(String locale) { this.locale = locale; }

    public Long getBankId() { return bankId; }
    public void setBankId(Long bankId) { this.bankId = bankId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getGroupedBody() { return groupedBody; }
    public void setGroupedBody(String groupedBody) { this.groupedBody = groupedBody; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isMandatory() { return mandatory; }
    public void setMandatory(boolean mandatory) { this.mandatory = mandatory; }

    public String getApprovedBy() { return approvedBy; }
    public void setApprovedBy(String approvedBy) { this.approvedBy = approvedBy; }

    public Instant getApprovedAt() { return approvedAt; }
    public void setApprovedAt(Instant approvedAt) { this.approvedAt = approvedAt; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Long getVersionLock() { return versionLock; }
    public void setVersionLock(Long versionLock) { this.versionLock = versionLock; }
}