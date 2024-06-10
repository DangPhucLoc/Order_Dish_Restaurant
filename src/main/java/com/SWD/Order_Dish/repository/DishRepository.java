package com.SWD.Order_Dish.repository;

import com.SWD.Order_Dish.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<DishEntity, String> {
}
