package com.faraz.razorpay.payment.dto.request;

import com.faraz.razorpay.common.entity.Money;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Map;

public record CreateOrderRequest(

        @NotNull(message = "Amount is required")
        Money amount,

        @Size(max = 100)
        String receipt, // orderId (known to merchant)

        Map<String, Object> notes,

        LocalDateTime expiresAt
) {


}
