package com.faraz.razorpay.payment.processor;

import com.faraz.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.faraz.razorpay.payment.processor.dto.PaymentProcessorResponse;

import java.io.IOException;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);


}
