package com.faraz.razorpay.vault.controller;

import com.faraz.razorpay.vault.dto.request.TokenizeRequest;
import com.faraz.razorpay.vault.dto.response.TokenizeResponse;
import com.faraz.razorpay.vault.service.VaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/vault")
public class VaultController {

    private final VaultService vaultService;

    UUID merchantId = UUID.fromString("5af2e3db-9a36-4d81-b000-a123018d1352"); // TODO: replace it with MerchantContext


    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse>  tokenize(@Valid @RequestBody TokenizeRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vaultService.tokenize(request, merchantId));
    }
}
