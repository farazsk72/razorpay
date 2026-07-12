package com.faraz.razorpay.merchant.service;

import com.faraz.razorpay.merchant.dto.request.LoginRequest;
import com.faraz.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.faraz.razorpay.merchant.dto.response.LoginResponse;
import com.faraz.razorpay.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {

    MerchantResponse signup(MerchantSignupRequest request);

    LoginResponse login(LoginRequest request);
}
