package com.SWD.Order_Dish.model.order;

import com.SWD.Order_Dish.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String orderId;

    private String orderName;

    private boolean haveDeposit;

    private double totalPrice;

    private double advance;

    private double remaining;

    private Status status;

    private Date createdDate;

    private String createdBy;

    private Date updatedDate;

    private String updatedBy;

    private String userId;

    private String tableId;
}
