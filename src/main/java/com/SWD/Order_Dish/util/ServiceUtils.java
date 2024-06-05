package com.SWD.Order_Dish.util;

import com.SWD.Order_Dish.repository.DishCategoryRepository;

import java.util.ArrayList;
import java.util.List;

public class ServiceUtils {

    public static List<String> errors = new ArrayList<>();

    public static void validateDishCategoryId(List<String> ids, DishCategoryRepository repository) {
        ids.forEach(id -> {
            if (repository.findById(id).isEmpty()) {
                errors.add("Dish Category with id " + id + "does not exists!");
            }
        });
    }
}
