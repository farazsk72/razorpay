package com.faraz.razorpay.merchant.mapper;

import com.faraz.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.faraz.razorpay.merchant.dto.response.MerchantResponse;
import com.faraz.razorpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {

    Merchant toEntityFromMerchantSignupRequest(MerchantSignupRequest merchantSignupRequest);

    MerchantResponse toMerchantResponse(Merchant merchant);
}
