package com.SWD.Order_Dish.repository;

import com.SWD.Order_Dish.entity.DishCategoryEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface DishCategoryRepository extends JpaRepository<DishCategoryEntity, String> {

}
