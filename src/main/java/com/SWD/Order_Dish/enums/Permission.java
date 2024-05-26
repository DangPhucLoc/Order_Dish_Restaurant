package com.SWD.Order_Dish.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {
    MANAGER_READ("manager:read"),
    MANAGER_CREATE("manager:create"),
    MANAGER_UPDATE("manager:update"),
    MANAGER_DELETE("manager:delete"),
    STAFF_READ("staff:read"),
    STAFF_CREATE("staff:create"),
    STAFF_UPDATE("staff:update"),
    STAFF_DELETE("staff:delete"),
    ;

    private final String permission;
}
