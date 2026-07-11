package com.faraz.razorpay.vault.service.impl;

import com.faraz.razorpay.common.entity.Money;
import com.faraz.razorpay.common.enums.CardBrand;
import com.faraz.razorpay.common.exception.ResourceNotFoundException;
import com.faraz.razorpay.common.util.RandomizerUtil;
import com.faraz.razorpay.payment.processor.PaymentProcessorRouter;
import com.faraz.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.faraz.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.faraz.razorpay.vault.config.VaultEncryptionConfig;
import com.faraz.razorpay.vault.dto.request.TokenizeRequest;
import com.faraz.razorpay.vault.dto.response.TokenizeResponse;
import com.faraz.razorpay.vault.entity.CardToken;
import com.faraz.razorpay.vault.entity.VaultCard;
import com.faraz.razorpay.vault.repository.CardTokenRepository;
import com.faraz.razorpay.vault.repository.VaultCardRepository;
import com.faraz.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final VaultCardRepository vaultCardRepository;
    private final CardTokenRepository cardTokenRepository;
    private final BytesEncryptor decEncryptor;
    private final PaymentProcessorRouter paymentProcessorRouter;

    @Override
    @Transactional
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {

        String lastFour = request.pan().substring(request.pan().length() - 4);
        String bin = request.pan().substring(0, 6);
        CardBrand cardBrand = detectBrand(request.pan());

        byte[] dek = KeyGenerators.secureRandom(32).generateKey();
        byte[] encryptedPan = VaultEncryptionConfig.panEncryptor(dek)
                .encrypt(request.pan().getBytes(StandardCharsets.UTF_8));
        byte[] encryptedDek = decEncryptor.encrypt(encryptedPan);

        VaultCard vaultCard = vaultCardRepository.save(VaultCard.builder()
                .brand(cardBrand)
                .expriyYear(request.expiryYear().toString())
                .expriyMonth(request.expiryMonth().toString())
                .bin(bin)
                .lastFour(lastFour)
                .encryptedPan(encryptedPan)
                .encryptedDek(encryptedDek)
                .cardHolderName(request.cardHolderName())
                .build());

        String token = "tok_" + RandomizerUtil.randomBase64(32);

        cardTokenRepository.save(CardToken.builder()
                .token(token)
                .vaultCard(vaultCard)
                .merchant(merchantId)
                .customer(request.customerId())
                .build());

        return new TokenizeResponse(token, lastFour, cardBrand, request.expiryMonth(), request.expiryYear());
    }

    @Override
    public PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails) {
        CardToken cardToken = cardTokenRepository.findByTokenAndRevokedAtIsNull(token)
                .orElseThrow(() -> new ResourceNotFoundException("CardToken", token));

        VaultCard vaultCard =  cardToken.getVaultCard();
        byte[] panBytes = null;

        try {

            byte[] dek = decEncryptor.decrypt(vaultCard.getEncryptedDek());
            panBytes = VaultEncryptionConfig.panEncryptor(dek).decrypt(vaultCard.getEncryptedPan());

            String pan = new String(panBytes, StandardCharsets.UTF_8);
            String expiry = vaultCard.getExpriyMonth() + "/" + vaultCard.getExpriyYear();

            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest
                    .card(paymentId, pan, expiry, amount, methodDetails);

            PaymentProcessorResponse response = paymentProcessorRouter.charge(paymentProcessorRequest);

            log.info("Vault charge registered, token={}*****", token.substring(0, 4));

            Arrays.fill(panBytes, (byte) 0);

            return response;

        }catch (Exception e) {
            log.warn("Vault charge failed, token={}*****", token.substring(0, 4));
            return new PaymentProcessorResponse.Failure("VAULT_CHARGE_ERROR", e.getMessage());
        }finally {
            if(panBytes != null) {
                Arrays.fill(panBytes, (byte) 0);
            }
        }
    }

    private CardBrand detectBrand(String pan) {

            if (pan.startsWith("4")) return CardBrand.VISA;
            if (pan.startsWith("5") || pan.startsWith("2")) return CardBrand.MASTERCARD;
            if (pan.startsWith("37") || pan.startsWith("34")) return CardBrand.AMEX;
            if (pan.startsWith("6")) return CardBrand.DISCOVER;

            return CardBrand.RUPAY;
        }
    }