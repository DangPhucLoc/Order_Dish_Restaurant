package com.SWD.Order_Dish.model.order;

import com.SWD.Order_Dish.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private String orderId;

    @NotNull(message = "Deposit status cannot be null")
    private boolean haveDeposit;

    @NotNull(message = "Total price cannot be null")
    private double totalPrice;

    private double advance;

    private double remaining;

    @NotNull(message = "Status cannot be null")
    private Status status;

    private String userId;

    private String tableId;
}
