package com.SWD.Order_Dish.model.table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableRequest {

    private String tableId;

    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Availability cannot be null")
    private Boolean isAvailable;

    @NotNull(message = "Area ID cannot be null")
    @NotBlank(message = "Area ID cannot be blank")
    private String areaId;
}