package com.faraz.razorpay.payment.service.impl;

import com.faraz.razorpay.common.enums.OrderStatus;
import com.faraz.razorpay.common.exception.BusinessRuleViolationException;
import com.faraz.razorpay.common.exception.DuplicateResourceException;
import com.faraz.razorpay.common.exception.ResourceNotFoundException;
import com.faraz.razorpay.payment.dto.request.CreateOrderRequest;
import com.faraz.razorpay.payment.dto.response.OrderResponse;
import com.faraz.razorpay.payment.dto.response.PaymentResponse;
import com.faraz.razorpay.payment.entity.OrderRecord;
import com.faraz.razorpay.payment.entity.Payment;
import com.faraz.razorpay.payment.mapper.OrderMapper;
import com.faraz.razorpay.payment.mapper.PaymentMapper;
import com.faraz.razorpay.payment.repository.OrderRepository;
import com.faraz.razorpay.payment.repository.PaymentRepository;
import com.faraz.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    @Value("${payment.order.default-order-expiry-minutes:15}")
    private int defaultOrderExpiryMinutes;

    @Override
    @Transactional
    public OrderResponse create(UUID merchantId, CreateOrderRequest request) {
        if(request.receipt() != null && orderRepository.existsByMerchantIdAndReceipt(merchantId, request.receipt())) {
            throw new DuplicateResourceException("DUPLICATE_ORDER_RECEIPT", "Order with receipt already exists: " + request.receipt());
        }

        OrderRecord order = OrderRecord.builder()
                .merchantId(merchantId)
                .amount(request.amount())
                .notes(request.notes())
                .receipt(request.receipt())
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() != null ? request.expiresAt() : LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes))
                .build();

        order = orderRepository.save(order);

        // TODO: publish kafka event about order creation

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancel(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        if(order.getOrderStatus() == OrderStatus.CANCELLED || order.getOrderStatus() == OrderStatus.PAID) {
            throw new BusinessRuleViolationException("CANNOT_CANCEL_ORDER",
                    "Cannot cancel an order that is already " + order.getOrderStatus());
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public List<PaymentResponse> listPayments(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

        List<Payment> paymentList = paymentRepository.findByOrder_Id(orderId);
//        return paymentList.stream().map(paymentMapper::toPaymentResponse).collect(Collectors.toList());

        return paymentMapper.toPaymentResponseList(paymentList);
    }
}
