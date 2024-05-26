package com.SWD.Order_Dish.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplateName {
    ACTIVATE_ACCOUNT("ACTIVATE_ACCOUNT"),
    RESET_PASSWORD("RESET_PASSWORD");
    private final String name;
}
