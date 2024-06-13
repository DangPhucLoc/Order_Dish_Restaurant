package com.SWD.Order_Dish.service;

import com.SWD.Order_Dish.entity.DishCategoryEntity;
import com.SWD.Order_Dish.entity.DishEntity;
import com.SWD.Order_Dish.exception.CustomValidationException;
import com.SWD.Order_Dish.model.dish.DishRequest;
import com.SWD.Order_Dish.model.dish.DishResponse;
import com.SWD.Order_Dish.repository.DishRepository;
import com.SWD.Order_Dish.repository.DishCategoryRepository;
import com.SWD.Order_Dish.util.S3Util;
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
public class DishService {

    private final Logger LOGGER = LoggerFactory.getLogger(DishService.class);
    private final DishRepository dishRepository;
    private final DishCategoryRepository dishCategoryRepository;

    public List<DishResponse> findAll() {
        LOGGER.info("Find all dishes");
        List<DishEntity> dishes = dishRepository.findAll();
        if (dishes.isEmpty()) {
            LOGGER.warn("No dishes were found!");
        }
        return dishes.stream()
                .map(this::dishResponseGenerator)
                .collect(Collectors.toList());
    }

    public DishResponse findById(String id) {
        LOGGER.info("Find dish with id " + id);
        Optional<DishEntity> dish = dishRepository.findById(id);
        if (dish.isEmpty()) {
            LOGGER.warn("No dish was found!");
            return null;
        }
        return dish.map(this::dishResponseGenerator).get();
    }

    public DishResponse save(DishRequest dishRequest) {
        DishEntity dish;
        Optional<DishCategoryEntity> dishCategory = dishCategoryRepository.findById(dishRequest.getDishCategoryId());
        if (dishCategory.isEmpty()) {
            throw new CustomValidationException(List.of("No dish category was found!"));
        }

        if (dishRequest.getDishId() != null) {
            LOGGER.info("Update dish with id " + dishRequest.getDishId());
            checkExist(dishRequest.getDishId());
            dish = dishRepository.findById(dishRequest.getDishId()).get();
            updateDish(dish, dishRequest);
            dishRepository.save(dish);
        } else {
            LOGGER.info("Create new dish");
            dish = createDish(dishRequest, dishCategory.get());
            dishRepository.save(dish);
        }
        return dishResponseGenerator(dish);
    }

    public void delete(String id) {
        if (id != null && !id.trim().isEmpty()) {
            LOGGER.info("Delete dish with id " + id);
            checkExist(id);
            DishEntity dish = dishRepository.findById(id).get();
            dishRepository.delete(dish);
        }
    }

    private DishEntity createDish(DishRequest request, DishCategoryEntity dishCategory) {
        DishEntity dish = new DishEntity();
        setCommonFields(dish, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        dish.setCreatedBy(authentication.getName());
        dish.setCreatedDate(new Date());
        dish.setDishCategoryEntity(dishCategory);
        dish.setImageURL(S3Util.uploadFile(request.getImageURL()));
        return dish;
    }

    private void updateDish(DishEntity dish, DishRequest request) {
        setCommonFields(dish, request);
        if(request.getImageURL() != null) {
            S3Util.deleteFile(dish.getImageURL());
            dish.setImageURL(S3Util.uploadFile(request.getImageURL()));
        }
        DishCategoryEntity dishCategory = dishCategoryRepository.findById(request.getDishCategoryId()).get();
        dish.setDishCategoryEntity(dishCategory);
    }

    private void setCommonFields(DishEntity dish, DishRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        dish.setName(request.getName());
        dish.setDishPrice(request.getDishPrice());
        dish.setIsAvailable(request.getIsAvailable());
        dish.setUpdatedBy(authentication.getName());
        dish.setUpdatedDate(new Date());
    }

    private DishResponse dishResponseGenerator(DishEntity dish) {
        DishResponse dishResponse = new DishResponse();
        dishResponse.setDishId(dish.getDishId());
        dishResponse.setImageURL(dish.getImageURL());
        dishResponse.setName(dish.getName());
        dishResponse.setDishPrice(dish.getDishPrice());
        dishResponse.setIsAvailable(dish.getIsAvailable());
        dishResponse.setCreatedDate(dish.getCreatedDate());
        dishResponse.setCreatedBy(dish.getCreatedBy());
        dishResponse.setUpdatedDate(dish.getUpdatedDate());
        dishResponse.setUpdatedBy(dish.getUpdatedBy());
        dishResponse.setDishCategoryId(dish.getDishCategoryEntity().getDishCateGoryId());
        return dishResponse;
    }

    private void checkExist(String id) {
        if (dishRepository.findById(id).isEmpty()) {
            LOGGER.error("No dish was found!");
            throw new CustomValidationException(List.of("No dish was found!"));
        }
    }
}
