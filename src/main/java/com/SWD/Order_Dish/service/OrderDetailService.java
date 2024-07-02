package com.SWD.Order_Dish.service;

import com.SWD.Order_Dish.entity.DishEntity;
import com.SWD.Order_Dish.entity.OrderDetailEntity;
import com.SWD.Order_Dish.entity.OrderEntity;
import com.SWD.Order_Dish.enums.Status;
import com.SWD.Order_Dish.model.orderdetail.OrderDetailRequest;
import com.SWD.Order_Dish.model.orderdetail.OrderDetailResponse;
import com.SWD.Order_Dish.repository.AccountRepository;
import com.SWD.Order_Dish.repository.DishRepository;
import com.SWD.Order_Dish.repository.OrderDetailRepository;
import com.SWD.Order_Dish.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    private final Logger LOGGER = LoggerFactory.getLogger(OrderDetailService.class);
    private final AccountRepository accountRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;


    public List<OrderDetailResponse> getAll(String orderId) {
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findAll();
        orderDetails = orderDetails.stream().filter(
                orderDetailEntity -> orderDetailEntity.getOrderEntity().getOrderId().equals(orderId)).toList();

        List<OrderDetailResponse> responses = new ArrayList<>();
        for (OrderDetailEntity orderDetailEntity : orderDetails) {
            OrderDetailResponse response = create(orderDetailEntity);
            if (response != null) {
                responses.add(response);
            }
        }
        return responses;
    }

    public OrderDetailResponse getById(String orderDetailId) {
        OrderDetailEntity orderDetailEntity = orderDetailRepository.findById(orderDetailId).orElse(null);
        if (orderDetailEntity == null) {
            LOGGER.warn("Order detail not found");
            return null;
        }
        return create(orderDetailEntity);
    }

//    public OrderDetailResponse save(OrderDetailRequest request) {
//
//    }

    private OrderDetailResponse create(OrderDetailEntity orderDetailEntity) {
        OrderEntity orderEntity = orderRepository.findById(orderDetailEntity.getOrderEntity().getOrderId()).orElse(null);
        if (orderEntity == null) {
            LOGGER.error("Order not found");
            return null;
        }

        DishEntity dishEntity = orderDetailEntity.getDishEntity();
        if (dishEntity == null) {
            LOGGER.error("Dish not found");
            return null;
        }

        OrderDetailResponse response = new OrderDetailResponse();
        response.setOrderDetailId(orderDetailEntity.getOrderDetailId());
        response.setDishId(dishEntity.getDishId());
        response.setOrderId(orderEntity.getOrderId());
        response.setQuantity(orderDetailEntity.getQuantity());
        response.setPrice(dishEntity.getDishPrice());
        response.setStatus(String.valueOf(orderDetailEntity.getStatus()));
        response.setDescription(orderDetailEntity.getDescription());
        response.setCreatedBy(orderDetailEntity.getCreatedBy());
        response.setCreatedDate(orderDetailEntity.getCreatedDate());
        response.setUpdatedDate(orderDetailEntity.getUpdatedDate());
        response.setLastModifiedBy(orderDetailEntity.getUpdatedBy());
        response.setDishName(dishEntity.getName());
        return response;
    }
}
