package com.SWD.Order_Dish.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SimpleDateValidator implements ConstraintValidator<ValidSimpleDate,String> {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public void initialize(ValidSimpleDate constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (date == null || date.isEmpty()) {
            return false; // or true, depending on your requirement
        }
        return isValidFormat(date);
    }

    private boolean isValidFormat(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
