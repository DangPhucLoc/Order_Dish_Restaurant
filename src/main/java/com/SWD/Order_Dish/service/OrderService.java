package com.SWD.Order_Dish.service;

import com.SWD.Order_Dish.entity.AccountEntity;
import com.SWD.Order_Dish.entity.OrderDetailEntity;
import com.SWD.Order_Dish.entity.OrderEntity;
import com.SWD.Order_Dish.entity.TableEntity;
import com.SWD.Order_Dish.exception.CustomValidationException;
import com.SWD.Order_Dish.model.order.OrderRequest;
import com.SWD.Order_Dish.model.order.OrderResponse;
import com.SWD.Order_Dish.model.orderdetail.OrderDetailRequest;
import com.SWD.Order_Dish.repository.AccountRepository;
import com.SWD.Order_Dish.repository.OrderDetailRepository;
import com.SWD.Order_Dish.repository.OrderRepository;
import com.SWD.Order_Dish.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final AccountRepository accountRepository;
    private final TableRepository tableRepository;

    public List<OrderResponse> findAll() {
        LOGGER.info("Find all orders");
        List<OrderEntity> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            LOGGER.warn("No orders were found!");
        }
        return orders.stream()
                .map(this::orderResponseGenerator)
                .collect(Collectors.toList());
    }

    public OrderResponse findById(String id) {
        LOGGER.info("Find order with id " + id);
        Optional<OrderEntity> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            LOGGER.warn("No order was found!");
            return null;
        }
        return order.map(this::orderResponseGenerator).get();
    }

    public List<OrderResponse> findByUserId(String userId) {
        LOGGER.info("Find orders by user id " + userId);
        List<OrderEntity> orders = orderRepository.findByAccountEntity_UserId(userId);
        if (orders.isEmpty()) {
            LOGGER.warn("No orders were found!");
        }
        return orders.stream()
                .map(this::orderResponseGenerator)
                .collect(Collectors.toList());
    }

    public OrderResponse save(OrderRequest orderRequest) {
        OrderEntity order;
        if (orderRequest.getOrderId() != null) {
            LOGGER.info("Update order with id " + orderRequest.getOrderId());
            checkExist(orderRequest.getOrderId());
            order = orderRepository.findById(orderRequest.getOrderId()).get();
            updateOrder(order, orderRequest);
            orderRepository.save(order);
            LOGGER.info("Update order details");
            if(!orderRequest.getOrderDetails().isEmpty()) {
                for(OrderDetailRequest orderDetailRequest : orderRequest.getOrderDetails()) {
                    OrderDetailEntity orderDetail = getOrderDetailEntity(orderDetailRequest, order);
                    orderDetailRepository.save(orderDetail);
                }
            }
        } else {
            LOGGER.info("Create new order");
            order = createOrder(orderRequest);
            orderRepository.save(order);
            LOGGER.info("Create new order details");
            for(OrderDetailRequest orderDetailRequest : orderRequest.getOrderDetails()) {
                OrderDetailEntity orderDetail = getOrderDetailEntity(orderDetailRequest, order);
                orderDetailRepository.save(orderDetail);
            }
        }
        return orderResponseGenerator(order);
    }

    private OrderDetailEntity getOrderDetailEntity(OrderDetailRequest orderDetailRequest, OrderEntity order) {
        OrderDetailEntity orderDetail = new OrderDetailEntity();
        orderDetail.setOrderEntity(order);
        orderDetail.setDishEntity(orderDetail.getDishEntity());
        orderDetail.setDescription(orderDetailRequest.getDescription());
        orderDetail.setStatus(orderDetailRequest.getStatus());
        orderDetail.setQuantity(orderDetailRequest.getQuantity());
        orderDetail.setUpdatedBy(orderDetailRequest.getPersonSaveId());
        orderDetail.setCreatedBy(orderDetailRequest.getPersonSaveId());
        return orderDetail;
    }

    public void delete(String id) {
        if (id != null && !id.trim().isEmpty()) {
            LOGGER.info("Delete order with id " + id);
            checkExist(id);
            OrderEntity order = orderRepository.findById(id).get();
            orderRepository.delete(order);
        }
    }

    private OrderEntity createOrder(OrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OrderEntity order = new OrderEntity();
        order.setName(request.getOrderName());
        order.setHaveDeposit(request.isHaveDeposit());
        order.setTotalPrice(request.getTotalPrice());
        order.setAdvance(request.getAdvance());
        order.setRemaining(request.getRemaining());
        order.setStatus(request.getStatus());
        order.setCreatedDate(new Date());
        order.setCreatedBy(authentication.getName());
        order.setUpdatedDate(new Date());
        order.setUpdatedBy(authentication.getName());
        order.setAccountEntity(getAccountEntity(request.getUserId()));
        order.setTableEntity(getTableEntity(request.getTableId()));
        return order;
    }

    private void updateOrder(OrderEntity order, OrderRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        order.setName(request.getOrderName());
        order.setHaveDeposit(request.isHaveDeposit());
        order.setTotalPrice(request.getTotalPrice());
        order.setAdvance(request.getAdvance());
        order.setRemaining(request.getRemaining());
        order.setStatus(request.getStatus());
        order.setUpdatedDate(new Date());
        order.setUpdatedBy(authentication.getName());
        order.setAccountEntity(getAccountEntity(request.getUserId()));
        order.setTableEntity(getTableEntity(request.getTableId()));
    }

    private AccountEntity getAccountEntity(String userId) {
        if (userId == null) {
            return null;
        }
        return accountRepository.findById(userId)
                .orElseThrow(() -> new CustomValidationException(List.of("No account was found!")));
    }

    private TableEntity getTableEntity(String tableId) {
        if (tableId == null) {
            return null;
        }
        return tableRepository.findById(tableId)
                .orElseThrow(() -> new CustomValidationException(List.of("No table was found!")));
    }

    private OrderResponse orderResponseGenerator(OrderEntity order) {
        return new OrderResponse(
                order.getOrderId(),
                order.getName(),
                order.isHaveDeposit(),
                order.getTotalPrice(),
                order.getAdvance(),
                order.getRemaining(),
                order.getStatus(),
                order.getCreatedDate(),
                order.getCreatedBy(),
                order.getUpdatedDate(),
                order.getUpdatedBy(),
                order.getAccountEntity() != null ? order.getAccountEntity().getUserId() : null,
                order.getTableEntity() != null ? order.getTableEntity().getTableId() : null
        );
    }

    private void checkExist(String id) {
        Optional<OrderEntity> order = orderRepository.findById(id);
        if (order.isEmpty()) {
            throw new CustomValidationException(List.of("No order was found!"));
        }
    }
}
