package com.faraz.razorpay.payment.service;

import com.faraz.razorpay.payment.dto.request.PaymentInitRequest;
import com.faraz.razorpay.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request);
}
