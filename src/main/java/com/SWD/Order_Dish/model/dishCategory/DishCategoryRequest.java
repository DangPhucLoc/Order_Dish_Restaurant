package com.SWD.Order_Dish.model.dishCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishCategoryRequest {

    private String dishCateGoryId;

    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    private String name;

    @NotNull(message = "can not be null")
    @NotBlank(message = "can not be blank")
    private String description;
}
