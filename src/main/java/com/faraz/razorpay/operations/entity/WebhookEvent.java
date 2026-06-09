package com.faraz.razorpay.operations.entity;

import com.faraz.razorpay.common.enums.WebhookStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "webhook_event")
public class WebhookEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchantId;

    @Column(nullable = false, length = 100)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> payload;

    @Column(nullable = false)
    private String targetUrl;

    @Column(nullable = false)
    private String signature;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WebhookStatus status;

    @Column(nullable = false)
    private Integer attempts = 0;

    private LocalDateTime nextRetryAt;

    private LocalDateTime lastAttemptAt;

    private Integer lastResponseCode;

    @Column(length = 1000)
    private String lastResponseBody;

    private LocalDateTime deliveredAt;
}
