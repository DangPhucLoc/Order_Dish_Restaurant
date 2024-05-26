package com.SWD.Order_Dish.repository;

import com.SWD.Order_Dish.entity.ConfirmationTokenEntity;
import com.SWD.Order_Dish.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationTokenEntity, String> {
    Optional<ConfirmationTokenEntity> findByTokenAndTokenType(String token, TokenType tokenType);
}
