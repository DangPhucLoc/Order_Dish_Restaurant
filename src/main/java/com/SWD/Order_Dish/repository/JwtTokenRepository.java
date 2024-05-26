package com.SWD.Order_Dish.repository;

import com.SWD.Order_Dish.entity.JwtTokenEntity;
import com.SWD.Order_Dish.enums.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtTokenEntity, String> {
    List<JwtTokenEntity> findAllByUserEntity_UserIdAndIsExpiredFalseAndIsRevokedFalse(String accountId);

    Optional<JwtTokenEntity> findByToken(String token);

    List<JwtTokenEntity> findAllByUserEntity_UserIdAndTokenType(String accountId, TokenType refreshToken);

    List<JwtTokenEntity> findAllByUserEntity_UserIdAndTokenTypeAndIsExpiredFalseAndIsRevokedFalse(String accountId,
                                                                                                        TokenType accessToken);
}
