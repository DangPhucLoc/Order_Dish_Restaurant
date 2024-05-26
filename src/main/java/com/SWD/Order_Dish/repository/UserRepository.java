package com.SWD.Order_Dish.repository;

import com.SWD.Order_Dish.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
    UserEntity findUserEntityByEmail(String email);
    UserEntity findUserEntityByPhoneNumber(String phone);
}
