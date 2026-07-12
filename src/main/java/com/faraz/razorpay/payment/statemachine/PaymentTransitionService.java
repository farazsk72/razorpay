package com.faraz.razorpay.payment.statemachine;

import com.faraz.razorpay.common.enums.PaymentActor;
import com.faraz.razorpay.common.enums.PaymentEvent;
import com.faraz.razorpay.common.enums.PaymentStatus;
import com.faraz.razorpay.payment.entity.Payment;
import com.faraz.razorpay.payment.entity.PaymentTransitionLog;
import com.faraz.razorpay.payment.repository.PaymentTransitionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentTransitionService {

    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine paymentStateMachine;

    public PaymentStatus apply(Payment payment, PaymentEvent event){
        PaymentStatus next = paymentStateMachine.transition(payment.getStatus(), event);
        PaymentTransitionLog log = PaymentTransitionLog.builder()
                .payment(payment)
                .fromStatus(payment.getStatus())
                .event(event)
                .toStatus(next)
                .actor(PaymentActor.SYSTEM)  // TODO: fetch merchant context to identify actor
                .occuredAt(LocalDateTime.now())
                .build();
        payment.setStatus(next);
        paymentTransitionLogRepository.save(log);
        return next;
    }


}
