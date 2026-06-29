package com.faraz.razorpay.payment.controller;

import com.faraz.razorpay.payment.dto.request.PaymentInitRequest;
import com.faraz.razorpay.payment.dto.response.PaymentResponse;
import com.faraz.razorpay.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    UUID merchantId = UUID.fromString("5af2e3db-9a36-4d81-b000-a123018d1352"); // TODO: replace it with MerchantContext

    @PostMapping
    public ResponseEntity<PaymentResponse> initiatePayment(@RequestBody @Valid PaymentInitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.initiate(merchantId, request));
    }


}
