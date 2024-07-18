package com.SWD.Order_Dish.model.orderdetail;

import com.SWD.Order_Dish.enums.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailRequest {
    private String orderDetailId;

    @NotNull(message = "cannot be null")
    @NotBlank(message = "cannot be blank")
    private String orderId;

    @NotNull(message = "cannot be null")
    @NotBlank(message = "cannot be blank")
    private String dishId;

    private String description;

    @NotNull(message = "cannot be null")
    @NotBlank(message = "cannot be blank")
    private Status status;

    @NotNull(message = "cannot be null")
    @Min(value = 1, message = "must be greater than 0")
    private Integer quantity;

    private String personSaveId;
}
