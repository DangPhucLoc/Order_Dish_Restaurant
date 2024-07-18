package com.SWD.Order_Dish.model.payment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private String paymentId;

    private double price;

    private Date paymentTime;

    private Boolean isAvailable;

    private Date createdDate;

    private String modifiedBy;

    private String orderId;
}
