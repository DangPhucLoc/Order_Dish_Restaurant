package com.SWD.Order_Dish.repository;

import com.SWD.Order_Dish.entity.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, String> {
    TableEntity findByTableId(String tableId);

    List<TableEntity> findByIsAvailable(Boolean isAvailable);

}
