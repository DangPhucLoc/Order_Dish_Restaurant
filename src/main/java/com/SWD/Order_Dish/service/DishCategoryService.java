package com.SWD.Order_Dish.service;

import com.SWD.Order_Dish.entity.DishCategoryEntity;
import com.SWD.Order_Dish.model.dishCategory.DishCategoryRequest;
import com.SWD.Order_Dish.model.dishCategory.DishCategoryResponse;
import com.SWD.Order_Dish.repository.DishCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishCategoryService {

    private final DishCategoryRepository dishCategoryRepository;

    public List<DishCategoryResponse> findAll() {
        List<DishCategoryEntity> dishCategories = dishCategoryRepository.findAll();
        return !dishCategories.isEmpty() ?
                dishCategories.stream()
                        .map(this::dishCategoryResponseGenerator)
                        .collect(Collectors.toList())
                : null;
    }

    public DishCategoryResponse findById(String id) {
        Optional<DishCategoryEntity> dishCategory = dishCategoryRepository.findById(id);
        return dishCategory.map(this::dishCategoryResponseGenerator).orElse(null);
    }

    public DishCategoryResponse save(DishCategoryRequest dishCategoryRequest) {
        DishCategoryEntity dishCategory;
        if (dishCategoryRequest.getDishCateGoryId() != null) {
            dishCategory =
                dishCategoryRepository.findById(dishCategoryRequest.getDishCateGoryId()).get();
            updateDishCategory(dishCategory, dishCategoryRequest);
            dishCategoryRepository.save(dishCategory);
        } else {
            dishCategory = createDishCategory(dishCategoryRequest);
            dishCategoryRepository.save(dishCategory);
        }
        return dishCategoryResponseGenerator(dishCategory);
    }

    public void delete(String id) {
        if (id != null && !id.trim().isEmpty()) {
            DishCategoryEntity dishCategory = dishCategoryRepository.findById(id).get();
            dishCategoryRepository.delete(dishCategory);
        }
    }

    private DishCategoryEntity createDishCategory(DishCategoryRequest request) {
        DishCategoryEntity dishCategory = new DishCategoryEntity();
        setCommonFields(dishCategory, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        dishCategory.setCreatedBy(authentication.getName());
        dishCategory.setCreatedDate(new Date());
        return dishCategory;
    }

    private void updateDishCategory(DishCategoryEntity dishCategory, DishCategoryRequest request) {
        setCommonFields(dishCategory, request);
    }

    private void setCommonFields(DishCategoryEntity dishCategory, DishCategoryRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        dishCategory.setName(request.getName());
        dishCategory.setDescription(request.getDescription());
        dishCategory.setUpdatedBy(authentication.getName());
        dishCategory.setUpdatedDate(new Date());
    }

    private DishCategoryResponse dishCategoryResponseGenerator(DishCategoryEntity dishCategory) {
        DishCategoryResponse dishCategoryResponse = new DishCategoryResponse();
        dishCategoryResponse.setDishCateGoryId(dishCategory.getDishCateGoryId());
        dishCategoryResponse.setName(dishCategory.getName());
        dishCategoryResponse.setDescription(dishCategory.getDescription());
        dishCategoryResponse.setCreatedDate(dishCategory.getCreatedDate());
        dishCategoryResponse.setCreatedBy(dishCategory.getCreatedBy());
        dishCategoryResponse.setUpdatedDate(dishCategory.getUpdatedDate());
        dishCategoryResponse.setUpdatedBy(dishCategory.getUpdatedBy());
        return dishCategoryResponse;
    }
}
