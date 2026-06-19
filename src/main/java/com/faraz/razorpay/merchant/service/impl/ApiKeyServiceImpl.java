package com.faraz.razorpay.merchant.service.impl;

import com.faraz.razorpay.common.exception.ResourceNotFoundException;
import com.faraz.razorpay.common.util.RandomizerUtil;
import com.faraz.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.faraz.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.faraz.razorpay.merchant.dto.response.ApiKeyResponse;
import com.faraz.razorpay.merchant.entity.ApiKey;
import com.faraz.razorpay.merchant.entity.Merchant;
import com.faraz.razorpay.merchant.repository.ApiKeyRepository;
import com.faraz.razorpay.merchant.repository.MerchantRepository;
import com.faraz.razorpay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Override
    public ApiKeyCreateResponse createApiKey(UUID merchantId, CreateApiKeyRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String keyId = "rzp_" + request.environment().name().toLowerCase()+"_"+ RandomizerUtil.randomBase64(24);
        String rawSecret = RandomizerUtil.randomBase64(40);
        // rawsecret = a-z, A-Z, 0-9, -, _
        // keyId = a-z, 0-9

        ApiKey apiKey = ApiKey.builder()
                .keyId(keyId)
                .keySecretHash(rawSecret) // TODO: encode with BcryptPasswordEncoder
                .environment(request.environment())
                .merchant(merchant)
                .build();

            apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), keyId, rawSecret, request.environment());
    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {
        return apiKeyRepository.findByMerchant_Id(merchantId).stream()
                .map(apiKey -> new ApiKeyResponse(
                        apiKey.getId(),
                        apiKey.getKeyId(),
                        apiKey.getEnvironment(),
                        apiKey.isEnabled(),
                        apiKey.getLastUsedAt(),
                        null))
                .toList();
    }

    @Override
    @Transactional
    public void revoke(UUID merchantId, UUID apiKeyId) {
        ApiKey key = apiKeyRepository.findById(apiKeyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", apiKeyId));
        key.setEnabled(false);
    }

    @Override
    public ApiKeyCreateResponse rotateKey(UUID merchantId, UUID apiKeyId) {
        ApiKey key = apiKeyRepository.findById(apiKeyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", apiKeyId));

        String newRawSecret = RandomizerUtil.randomBase64(40);
        key.setPreviousKeySecretHash(key.getKeySecretHash());
        key.setKeySecretHash(newRawSecret); // TODO: encode with BcryptPasswordEncoder
        key.setRotatedAt(java.time.LocalDateTime.now());
        key.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));
        key = apiKeyRepository.save(key);
        return new ApiKeyCreateResponse(
                key.getId(),
                key.getKeyId(),
                newRawSecret,
                key.getEnvironment());
    }
}
