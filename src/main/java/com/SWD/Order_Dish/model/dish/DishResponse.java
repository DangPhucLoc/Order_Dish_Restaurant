package com.SWD.Order_Dish.model.dish;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishResponse {

    private String dishId;

    private String imageURL;

    private String name;

    private double dishPrice;

    private Boolean isAvailable;

    private Date createdDate;

    private String createdBy;

    private Date updatedDate;

    private String updatedBy;

    private String dishCategoryId;
}
