package com.SWD.Order_Dish.exception;

import java.util.List;

public class CustomValidationException extends RuntimeException {
    private final List<String> errors;

    public CustomValidationException(List<String> errors) {
        super(String.join(", ", errors));
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}