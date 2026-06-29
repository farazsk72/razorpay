package com.faraz.razorpay.payment.gateway;

import com.faraz.razorpay.payment.gateway.dto.PaymentRequest;
import com.faraz.razorpay.payment.gateway.dto.PaymentResult;

public interface PaymentAdapter {

    PaymentResult initiate(PaymentRequest request);
}
