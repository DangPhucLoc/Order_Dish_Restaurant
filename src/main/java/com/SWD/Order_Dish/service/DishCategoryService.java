package com.SWD.Order_Dish.service;

import com.SWD.Order_Dish.entity.DishCategoryEntity;
import com.SWD.Order_Dish.exception.CustomValidationException;
import com.SWD.Order_Dish.model.dishCategory.DishCategoryRequest;
import com.SWD.Order_Dish.model.dishCategory.DishCategoryResponse;
import com.SWD.Order_Dish.repository.DishCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger LOGGER = LoggerFactory.getLogger(DishCategoryService.class);
    private final DishCategoryRepository dishCategoryRepository;

    public List<DishCategoryResponse> findAll() {
        LOGGER.info("Find all dish category");
        List<DishCategoryEntity> dishCategories = dishCategoryRepository.findAll();
        if (dishCategories.isEmpty()) {
            LOGGER.warn("No dish category was found!");
        }
        return dishCategories.stream()
                .map(this::dishCategoryResponseGenerator)
                .collect(Collectors.toList());
    }

    public DishCategoryResponse findById(String id) {
        LOGGER.info("Find dish category with id " + id);
        Optional<DishCategoryEntity> dishCategory = dishCategoryRepository.findById(id);
        if (dishCategory.isEmpty()) {
            LOGGER.warn("No dish category was found!");
            return null;
        }
        return dishCategory.map(this::dishCategoryResponseGenerator).get();
    }

    public DishCategoryResponse save(DishCategoryRequest dishCategoryRequest) {

        DishCategoryEntity dishCategory;

        if (dishCategoryRequest.getDishCateGoryId() != null) {
            LOGGER.info("Update dish category with id " + dishCategoryRequest.getDishCateGoryId());
            checkExist(dishCategoryRequest.getDishCateGoryId());
            dishCategory =
                dishCategoryRepository.findById(dishCategoryRequest.getDishCateGoryId()).get();
            updateDishCategory(dishCategory, dishCategoryRequest);
            dishCategoryRepository.save(dishCategory);
        } else {
            LOGGER.info("Create new dish category");
            dishCategory = createDishCategory(dishCategoryRequest);
            dishCategoryRepository.save(dishCategory);
        }
        return dishCategoryResponseGenerator(dishCategory);
    }

    public void delete(String id) {
        if (id != null && !id.trim().isEmpty()) {
            LOGGER.info("Delete dish category with id " + id);
            checkExist(id);
            DishCategoryEntity dishCategory = dishCategoryRepository.findById(id).get();
            dishCategoryRepository.delete(dishCategory);
        }
    }

    private DishCategoryEntity createDishCategory(DishCategoryRequest request) {
        DishCategoryEntity dishCategory = new DishCategoryEntity();
        updateDishCategory(dishCategory, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        dishCategory.setCreatedBy(authentication.getName());
        dishCategory.setCreatedDate(new Date());
        return dishCategory;
    }

    private void updateDishCategory(DishCategoryEntity dishCategory, DishCategoryRequest request) {
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

    private void checkExist(String id) {
        if (dishCategoryRepository.findById(id).isEmpty()) {
            LOGGER.error("No dish category was found!");
            throw new CustomValidationException(List.of("No dish category was found!"));
        }
    }
}
