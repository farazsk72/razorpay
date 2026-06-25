package com.faraz.razorpay.payment.mapper;

import com.faraz.razorpay.payment.dto.response.OrderResponse;
import com.faraz.razorpay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toOrderResponse(OrderRecord orderRecord);
}
