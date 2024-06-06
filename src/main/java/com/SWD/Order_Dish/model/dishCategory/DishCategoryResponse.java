package com.SWD.Order_Dish.model.dishCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishCategoryResponse {


    private String dishCateGoryId;

    private String name;

    private String description;

    private Date createdDate;

    private String createdBy;

    private Date updatedDate;

    private String updatedBy;
}
