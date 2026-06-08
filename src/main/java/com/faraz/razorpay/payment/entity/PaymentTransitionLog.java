package com.faraz.razorpay.payment.entity;

import com.faraz.razorpay.common.enums.PaymentActor;
import com.faraz.razorpay.common.enums.PaymentEvent;
import com.faraz.razorpay.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payment_transition_log")
public class PaymentTransitionLog {

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
