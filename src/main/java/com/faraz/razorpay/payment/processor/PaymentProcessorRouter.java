package com.faraz.razorpay.payment.processor;

import com.faraz.razorpay.common.enums.PaymentMethod;
import com.faraz.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.faraz.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentProcessorRouter {

    private final Map<PaymentMethod, PaymentProcessor> paymentProcessorMap;

    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        PaymentProcessor processor = paymentProcessorMap.get(request.method());
        if (processor == null) {
            throw new IllegalArgumentException("No payment processor found for method: " + request.method());
        }
        return processor.charge(request);
    }
}
