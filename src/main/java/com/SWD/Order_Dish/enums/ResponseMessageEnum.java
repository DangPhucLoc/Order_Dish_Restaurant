package com.SWD.Order_Dish.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseMessageEnum {
    UPDATE_SUCCESS("Update successfully"),
    UPDATE_FAIL("Update fail"),
    ADD_SUCCESS("Add successfully"),
    ADD_FAIL("Add fail"),
    INVALID_REQUEST("Invalid Request"),
    EXPIRED_JWT("Your access is not longer valid"),
    DUPLICATED_PHONE_NUMBER("This phone number has been used"),
    INVALID_ACCOUNT_ID("This account Id is invalid"),
    DUPLICATED_EMAIL("This email has been used before"),
    ACCOUNT_CREATED_SUCCESS("Account have created successfully");
    private final String detail;
}
