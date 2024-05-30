package com.SWD.Order_Dish.entity;

import com.SWD.Order_Dish.config.CustomerUUIDGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PAYMENT")
public class PaymentEntity {
    @Id
    @GeneratedValue(generator = "custom-id")
    @GenericGenerator(name = "custom-id", type = CustomerUUIDGenerator.class)
    @Column(name = "PAYMENT_ID", length = 36)
    private String paymentId;

    @Column(name = "PRICE", nullable = false)
    private double price;

    @Column(name = "PAYMENT_TIME", nullable = false)
    private Date paymentTime;

    @Column(name = "IS_AVAILABLE", nullable = false)
    private Boolean isAvailable;

    @Column(name = "CREATED_DATE", nullable = false)
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "MODIFIED_BY", nullable = false)
    private String modifiedBy;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity orderEntity;

}
