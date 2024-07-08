package com.SWD.Order_Dish.repository;

import com.SWD.Order_Dish.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, String> {
    List<OrderDetailEntity> findByAccountEntity_AccountId(String accountId);

}
