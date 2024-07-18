package com.SWD.Order_Dish.service;

import com.SWD.Order_Dish.entity.AccountEntity;
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
// get orderDetails by accountId
    public List<OrderDetailResponse> getByAccountId(String accountId) {
        LOGGER.info("get order detail by account id");
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findByAccountEntity_UserId(accountId);
        List<OrderDetailResponse> responses = new ArrayList<>();
        for (OrderDetailEntity orderDetailEntity : orderDetails) {
            OrderDetailResponse response = create(orderDetailEntity);
            if (response != null) {
                responses.add(response);
            }
        }
        return responses;
    }

    public List<OrderDetailResponse> getAll(){
        LOGGER.info("get all order detail");
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findAll();
        List<OrderDetailResponse> responses = new ArrayList<>();
        for (OrderDetailEntity orderDetailEntity : orderDetails) {
            OrderDetailResponse response = create(orderDetailEntity);
            if (response != null) {
                responses.add(response);
            }
        }
        return responses;
    }

// get all orderDetail by orderId
    public List<OrderDetailResponse> getByOrderId(String orderId) {
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
    // get by orederDetailId
    public OrderDetailResponse getById(String orderDetailId) {
        OrderDetailEntity orderDetailEntity = orderDetailRepository.findById(orderDetailId).orElse(null);
        if (orderDetailEntity == null) {
            LOGGER.warn("Order detail not found");
            return null;
        }
        return create(orderDetailEntity);
    }
//add new orderDetail
    public OrderDetailResponse save(OrderDetailRequest request) {
    LOGGER.info("save order detail");
        OrderEntity order = (orderRepository.findById(request.getOrderId()).orElse(null));
        if(order == null){
            LOGGER.error("Order not found");
            return null;
        }
        DishEntity dish = (dishRepository.findById(request.getDishId()).orElse(null));
        if(dish == null){
            LOGGER.error("Dish not found");
            return null;
        }
        AccountEntity account = accountRepository.findById(request.getPersonSaveId()).orElse(null);
        if (account == null) {
            LOGGER.error("Account not found");
            return null;
        }
        if(request.getOrderDetailId() != null) {
            OrderDetailEntity checkOrderDetail = orderDetailRepository.findById(request.getOrderDetailId()).orElse(null);
            if (checkOrderDetail != null) {
                LOGGER.info("update order detail");
                checkOrderDetail.setOrderEntity(order);
                checkOrderDetail.setDescription(request.getDescription());
                checkOrderDetail.setStatus(request.getStatus());
                checkOrderDetail.setQuantity(request.getQuantity());
                checkOrderDetail.setCreatedBy(request.getPersonSaveId());
                checkOrderDetail.setUpdatedBy(request.getPersonSaveId());
                checkOrderDetail.setDishEntity(dish);
                checkOrderDetail.setAccountEntity(account);
                orderDetailRepository.save(checkOrderDetail);
            }
        }
    OrderDetailEntity orderDetailEntity = new OrderDetailEntity();
    orderDetailEntity.setOrderEntity(order);
    orderDetailEntity.setDescription(request.getDescription());
    orderDetailEntity.setStatus(request.getStatus());
    orderDetailEntity.setQuantity(request.getQuantity());
    orderDetailEntity.setCreatedBy(request.getPersonSaveId());
    orderDetailEntity.setUpdatedBy(request.getPersonSaveId());
    orderDetailEntity.setDishEntity(dish);
    orderDetailEntity.setAccountEntity(account);

    orderDetailRepository.save(orderDetailEntity);
    return create(orderDetailEntity);

    }
// delete orderDetail
    public void deleteOrderDetail(String id) {
        LOGGER.info("delete order detail");
        if(orderDetailRepository.findById(id).isEmpty()){
           LOGGER.error("Order detail not found");
        }
        orderDetailRepository.deleteById(id);
    }
// update status of orderDetail
    public OrderDetailResponse updateStatus(String orderDetailId, String  statusStr) {
        LOGGER.info("update status order detail");
        Status status;
        try {
            status = Status.valueOf(statusStr.toUpperCase()); // Convert string to enum, assuming input is case-insensitive
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid status: {}", statusStr);
           throw new IllegalArgumentException("Invalid status: "+statusStr);
        }
        OrderDetailEntity orderDetailEntity = orderDetailRepository.findById(orderDetailId).orElse(null);
        if (orderDetailEntity == null) {
            LOGGER.error("Order detail not found");
            return null;
        }
        orderDetailEntity.setStatus(status);
        orderDetailRepository.save(orderDetailEntity);
        return create(orderDetailEntity);
    }
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
