package com.faraz.razorpay.merchant.service.impl;

import com.faraz.razorpay.common.enums.MerchantStatus;
import com.faraz.razorpay.common.enums.UserRole;
import com.faraz.razorpay.common.exception.DuplicateResourceException;
import com.faraz.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.faraz.razorpay.merchant.dto.response.MerchantResponse;
import com.faraz.razorpay.merchant.entity.AppUser;
import com.faraz.razorpay.merchant.entity.Merchant;
import com.faraz.razorpay.merchant.mapper.MerchantMapper;
import com.faraz.razorpay.merchant.repository.AppUserRepository;
import com.faraz.razorpay.merchant.repository.MerchantRepository;
import com.faraz.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository  merchantRepository;
    private final AppUserRepository appUserRepository;
    private final MerchantMapper merchantMapper;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {
        if(merchantRepository.existsByEmail(request.email())){
            log.error("Merchant with email {} already exists", request.email());
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL",
                    "Merchant with email already exists: "+request.email());
        }

        Merchant merchant = merchantMapper.toEntityFromMerchantSignupRequest(request);
        merchant.setStatus(MerchantStatus.PENDING_KYC);

        merchant = merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .passwordHash(request.password()) // TODO: encrypt using Bcrypt
                .merchant(merchant)
                .role(UserRole.OWNER)
                .build();
        appUserRepository.save(appUser);


        return merchantMapper.toMerchantResponse(merchant);
    }
}
