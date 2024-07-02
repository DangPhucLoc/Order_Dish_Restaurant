package com.SWD.Order_Dish.repository;

import com.SWD.Order_Dish.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findByAccountEntity_UserId(String userId);
}
