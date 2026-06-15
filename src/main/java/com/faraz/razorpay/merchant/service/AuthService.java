package com.faraz.razorpay.merchant.service;

import com.faraz.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.faraz.razorpay.merchant.dto.response.MerchantResponse;

public interface AuthService {

    MerchantResponse signup(MerchantSignupRequest request);

}
