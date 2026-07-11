package com.faraz.razorpay.vault.service;

import com.faraz.razorpay.common.entity.Money;
import com.faraz.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.faraz.razorpay.vault.dto.request.TokenizeRequest;
import com.faraz.razorpay.vault.dto.response.TokenizeResponse;
import jakarta.validation.Valid;

import java.util.Map;
import java.util.UUID;

public interface VaultService {

    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> stringObjectMap);
}
