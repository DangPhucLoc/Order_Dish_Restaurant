package com.SWD.Order_Dish.model._commonresponse;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
@Getter
@Builder
public class ResponseDTO {
    private Object content;
    private List<String> details;
    private int statusCode;
    private MetaDataDTO metaDataDTO;
}
