package com.SWD.Order_Dish.model.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private String paymentId;

    @NotNull(message = "Price cannot be null")
    @Min(value = 1, message = "Price must be at least 1")
    private double price;

    @NotNull(message = "Payment time cannot be null")
    private Date paymentTime;

    @NotNull(message = "Is available cannot be null")
    private Boolean isAvailable;

    private String orderId;
}
