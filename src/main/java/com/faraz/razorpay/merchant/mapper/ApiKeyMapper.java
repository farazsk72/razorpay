package com.faraz.razorpay.merchant.mapper;

import com.faraz.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.faraz.razorpay.merchant.dto.response.ApiKeyResponse;
import com.faraz.razorpay.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

    ApiKeyCreateResponse toApiKeyCreateResponse(ApiKey apiKey);

    List<ApiKeyResponse> toApiKeyResponseList(List<ApiKey> apiKeys);
}
