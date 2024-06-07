package com.SWD.Order_Dish.model.area;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaResponse {

    private String areaId;

    private String name;

    private Boolean isAvailable;

    private Date createdDate;

    private String createdBy;

    private Date updatedDate;

    private String updatedBy;

    private List<String> tableIdList;
}