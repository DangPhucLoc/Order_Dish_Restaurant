package com.SWD.Order_Dish.model.orderdetail;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private String orderDetailId;
    private String orderId;
    private String dishId;
    private int quantity;
    private double price;
    private String description;
    private String dishName;
    private String status;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String lastModifiedBy;

}
