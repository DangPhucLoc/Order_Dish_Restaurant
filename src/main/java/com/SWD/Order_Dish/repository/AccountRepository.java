package com.SWD.Order_Dish.repository;

import com.SWD.Order_Dish.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByEmail(String email);
    AccountEntity findAccountEntityByEmail(String email);
    AccountEntity findAccountEntityByPhoneNumber(String phone);
}
