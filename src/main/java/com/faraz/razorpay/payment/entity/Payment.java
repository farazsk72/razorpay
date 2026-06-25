package com.faraz.razorpay.payment.entity;

import com.faraz.razorpay.common.entity.Money;
import com.faraz.razorpay.common.enums.PaymentMethod;
import com.faraz.razorpay.common.enums.PaymentStatus;
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
@Table(name = "payment",
        indexes = {
        @Index(name = "idx_payment_order_id", columnList = "order_id"),

        }
)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderRecord order;

    @Column(nullable = false)
    private UUID merchantId;

    @Embedded
    private Money amount;

    @Column(nullable = false)
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> methodDetails;

    @Column(length = 100)
    private String bankReference;

    @Column(length = 10)
    private String errorCode;

    @Column(length = 200)
    private String errorDescription;

    private LocalDateTime authorizedAt;
    private LocalDateTime capturedAt;
    private LocalDateTime failedAt;
    private LocalDateTime refundedAt;
    private LocalDateTime settledAt;

}
