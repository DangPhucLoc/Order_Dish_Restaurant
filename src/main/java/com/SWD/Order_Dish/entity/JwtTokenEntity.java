package com.SWD.Order_Dish.entity;

import com.SWD.Order_Dish.config.CustomerUUIDGenerator;
import com.SWD.Order_Dish.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "JWT_TOKEN")
public class JwtTokenEntity {
    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", type = CustomerUUIDGenerator.class)
    @Column(name = "JWT_TOKEN_ID", length = 36)
    private String jwtTokenId;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "TOKEN_TYPE")
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = "IS_EXPIRED")
    private Boolean isExpired;

    @Column(name = "IS_REVOKED")
    private Boolean isRevoked;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity userEntity;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        JwtTokenEntity that = (JwtTokenEntity) o;
        return getJwtTokenId() != null && Objects.equals(getJwtTokenId(), that.getJwtTokenId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
