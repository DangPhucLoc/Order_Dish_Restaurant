package com.SWD.Order_Dish.repository;

import com.SWD.Order_Dish.entity.AreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaRepository extends JpaRepository<AreaEntity, String> {

}