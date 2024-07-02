package com.SWD.Order_Dish.repository;

import com.SWD.Order_Dish.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    Optional<AccountEntity> findByEmail(String email);
    AccountEntity findAccountEntityByEmail(String email);
    AccountEntity findAccountEntityByPhoneNumber(String phone);
    @Query(value = "SELECT * FROM ACCOUNT ac " +
            "WHERE (:email IS NULL OR LOWER(ac.email) LIKE LOWER(CONCAT('%', :email,'%'))) " +
            "AND (:phoneNumber IS NULL OR ac.phone_number LIKE CONCAT('%', :phoneNumber,'%')) " +
            "AND (:name IS NULL OR LOWER(ac.fullname) LIKE LOWER(CONCAT('%', :name,'%'))) " +
            "AND (:address IS NULL OR LOWER(ac.address) LIKE LOWER(CONCAT('%', :address,'%'))) " +
            "AND (:role IS NULL OR LOWER(ac.role) LIKE LOWER(CONCAT('%', :role,'%'))) ",
            nativeQuery = true)
    List<AccountEntity> searchSortFilter(@Param("email") String email,
                                         @Param("phoneNumber") String phoneNumber,
                                         @Param("name") String name,
                                         @Param("address") String address,
                                         @Param("role") String role);
}
