package com.SWD.Order_Dish.model.dish;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishRequest {

    private String dishId;

    private MultipartFile imageURL;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Ingredients cannot be null")
    @NotBlank(message = "Ingredients cannot be blank")
    private String ingredients;

    @NotNull(message = "Dish price cannot be null")
    @Min(value = 1, message = "Dish price must be at least 1")
    private double dishPrice;

    @NotNull(message = "Availability cannot be null")
    private Boolean isAvailable;

    @NotNull(message = "Dish Category ID cannot be null")
    @NotBlank(message = "Dish Category ID cannot be blank")
    private String dishCategoryId;
}
