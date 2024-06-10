package com.SWD.Order_Dish.model.dish;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishRequest {

    private String dishId;

    @NotNull(message = "Image URL cannot be null")
    @NotBlank(message = "Image URL cannot be blank")
    private String imageURL;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Dish price cannot be null")
    private double dishPrice;

    @NotNull(message = "Availability cannot be null")
    private Boolean isAvailable;

    @NotNull(message = "Dish Category ID cannot be null")
    @NotBlank(message = "Dish Category ID cannot be blank")
    private String dishCategoryId;
}
