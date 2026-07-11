package com.faraz.razorpay.vault.dto.response;

import com.faraz.razorpay.common.enums.CardBrand;

public record TokenizeResponse(

        String token,
        String lastFour,
        CardBrand brand,
        Integer expiryMonth,
        Integer expiryYear
) {
}
