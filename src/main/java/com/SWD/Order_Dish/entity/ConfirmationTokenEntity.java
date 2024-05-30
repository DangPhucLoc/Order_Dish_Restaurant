package com.SWD.Order_Dish.entity;

import com.SWD.Order_Dish.config.CustomerUUIDGenerator;
import com.SWD.Order_Dish.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@AllArgsConstructor
@Table(name = "CONFIRMATION_TOKEN")
public class ConfirmationTokenEntity {
    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", type = CustomerUUIDGenerator.class)
    @Column(name = "CONFIRMATION_TOKEN_ID", length = 36)
    private String confirmationTokenId;

    @Column(name = "TOKEN", nullable = false)
    private String token;
    @Column(name = "TOKEN_TYPE")
    private TokenType tokenType;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "EXPIRED_AT", nullable = false)
    private LocalDateTime expiredAt;
    @Column(name = "VALIDATED_AT")
    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private AccountEntity accountEntity;
}
