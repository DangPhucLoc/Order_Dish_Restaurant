package com.SWD.Order_Dish.repository;

import com.SWD.Order_Dish.entity.DishCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DishCategoryRepository extends JpaRepository<DishCategoryEntity, String> {

}
