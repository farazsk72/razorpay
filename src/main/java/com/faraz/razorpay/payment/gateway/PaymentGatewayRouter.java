package com.faraz.razorpay.payment.gateway;

import com.faraz.razorpay.common.enums.PaymentMethod;
import com.faraz.razorpay.payment.gateway.dto.PaymentRequest;
import com.faraz.razorpay.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {

    private final Map<PaymentMethod, PaymentAdapter> paymentAdapters;

    public PaymentResult initiate(PaymentRequest request) {

        PaymentAdapter adapter = paymentAdapters.get(request.method());
        if(adapter == null) {
            throw new IllegalArgumentException("No payment adapter registered for method: " + request.method());
        }
        adapter.initiate(request);

        return null;
    }
}
