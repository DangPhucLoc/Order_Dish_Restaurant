package com.SWD.Order_Dish.service;

import com.SWD.Order_Dish.entity.OrderEntity;
import com.SWD.Order_Dish.entity.PaymentEntity;
import com.SWD.Order_Dish.exception.CustomValidationException;
import com.SWD.Order_Dish.model.payment.PaymentRequest;
import com.SWD.Order_Dish.model.payment.PaymentResponse;
import com.SWD.Order_Dish.repository.OrderRepository;
import com.SWD.Order_Dish.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public List<PaymentResponse> findAll() {
        LOGGER.info("Find all payments");
        List<PaymentEntity> payments = paymentRepository.findAll();
        if (payments.isEmpty()) {
            LOGGER.warn("No payments were found!");
        }
        return payments.stream()
                .map(this::paymentResponseGenerator)
                .collect(Collectors.toList());
    }

    public PaymentResponse findById(String id) {
        LOGGER.info("Find payment with id " + id);
        Optional<PaymentEntity> payment = paymentRepository.findById(id);
        if (payment.isEmpty()) {
            LOGGER.warn("No payment was found!");
            return null;
        }
        return payment.map(this::paymentResponseGenerator).get();
    }

    public PaymentResponse save(PaymentRequest paymentRequest) {
        PaymentEntity payment;
        if (paymentRequest.getPaymentId() != null) {
            LOGGER.info("Update payment with id " + paymentRequest.getPaymentId());
            checkExist(paymentRequest.getPaymentId());
            payment = paymentRepository.findById(paymentRequest.getPaymentId()).get();
            updatePayment(payment, paymentRequest);
            paymentRepository.save(payment);
        } else {
            LOGGER.info("Create new payment");
            payment = createPayment(paymentRequest);
            paymentRepository.save(payment);
        }
        return paymentResponseGenerator(payment);
    }

    public void delete(String id) {
        if (id != null && !id.trim().isEmpty()) {
            LOGGER.info("Delete payment with id " + id);
            checkExist(id);
            PaymentEntity payment = paymentRepository.findById(id).get();
            paymentRepository.delete(payment);
        }
    }

    private PaymentEntity createPayment(PaymentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PaymentEntity payment = new PaymentEntity();
        payment.setPrice(request.getPrice());
        payment.setPaymentTime(request.getPaymentTime());
        payment.setIsAvailable(request.getIsAvailable());
        payment.setCreatedDate(new Date());
        payment.setModifiedBy(authentication.getName());
        payment.setOrderEntity(getOrderEntity(request.getOrderId()));
        return payment;
    }

    private void updatePayment(PaymentEntity payment, PaymentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        payment.setPrice(request.getPrice());
        payment.setPaymentTime(request.getPaymentTime());
        payment.setIsAvailable(request.getIsAvailable());
        payment.setModifiedBy(authentication.getName());
        payment.setOrderEntity(getOrderEntity(request.getOrderId()));
    }

    private OrderEntity getOrderEntity(String orderId) {
        if (orderId == null) {
            return null;
        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomValidationException(List.of("No order was found!")));
    }

    private PaymentResponse paymentResponseGenerator(PaymentEntity payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getPrice(),
                payment.getPaymentTime(),
                payment.getIsAvailable(),
                payment.getCreatedDate(),
                payment.getModifiedBy(),
                payment.getOrderEntity() != null ? payment.getOrderEntity().getOrderId() : null
        );
    }

    private void checkExist(String id) {
        Optional<PaymentEntity> payment = paymentRepository.findById(id);
        if (payment.isEmpty()) {
            throw new CustomValidationException(List.of("No payment was found!"));
        }
    }
}
