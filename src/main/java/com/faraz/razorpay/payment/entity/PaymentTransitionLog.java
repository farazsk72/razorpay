package com.faraz.razorpay.payment.entity;

import com.faraz.razorpay.common.entity.BaseEntity;
import com.faraz.razorpay.common.enums.PaymentActor;
import com.faraz.razorpay.common.enums.PaymentEvent;
import com.faraz.razorpay.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "payment_transition_log",
        indexes = {
                @Index(name = "idx_payment_transition_log_payment_id", columnList = "payment_id"),
        }
)
public class PaymentTransitionLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", length = 20)
    private PaymentStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", length = 20)
    private PaymentStatus toStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "event", length = 50)
    private PaymentEvent event;

    @Enumerated(EnumType.STRING)
    @Column(name = "actor", length = 10)
    private PaymentActor actor;

    @Column(name = "occured_at", nullable = false)
    private LocalDateTime occuredAt;

}
