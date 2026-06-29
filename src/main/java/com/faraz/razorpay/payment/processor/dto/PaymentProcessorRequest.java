package com.faraz.razorpay.payment.processor.dto;

import com.faraz.razorpay.common.entity.Money;
import com.faraz.razorpay.common.enums.PaymentMethod;

import java.util.Map;

public record PaymentProcessorRequest(

        PaymentMethod method,
        Money amount,
        String pan,
        Map<String, Object> methodDetails
) {
}
