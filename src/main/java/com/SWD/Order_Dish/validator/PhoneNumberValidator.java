package com.SWD.Order_Dish.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    private Pattern pattern;

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        this.pattern = Pattern.compile(constraintAnnotation.pattern());
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return true; // Consider null as valid, or change to false if required
        }
        return pattern.matcher(phoneNumber).matches();
    }
}
