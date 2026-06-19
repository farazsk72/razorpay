package com.faraz.razorpay.merchant.service;

import com.faraz.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.faraz.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.faraz.razorpay.merchant.dto.response.ApiKeyResponse;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {

    ApiKeyCreateResponse createApiKey(UUID merchantId, CreateApiKeyRequest request);

    List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID apiKeyId);

    ApiKeyCreateResponse rotateKey(UUID merchantId, UUID apiKeyId);
}
