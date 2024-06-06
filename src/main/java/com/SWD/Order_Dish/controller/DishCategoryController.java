package com.SWD.Order_Dish.controller;

import com.SWD.Order_Dish.model._commonresponse.ResponseDTO;
import com.SWD.Order_Dish.model.dishCategory.DishCategoryRequest;
import com.SWD.Order_Dish.model.dishCategory.DishCategoryResponse;
import com.SWD.Order_Dish.service.DishCategoryService;
import com.SWD.Order_Dish.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dishCategory")
@RequiredArgsConstructor
@Validated
public class DishCategoryController {
    private final DishCategoryService dishCategoryService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        List<DishCategoryResponse> result = dishCategoryService.findAll();
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable String id) {
        DishCategoryResponse result = dishCategoryService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> update(@Valid @RequestBody DishCategoryRequest request) {
        DishCategoryResponse result = dishCategoryService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@Valid @RequestBody DishCategoryRequest request) {
        DishCategoryResponse result = dishCategoryService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable String id) {
        dishCategoryService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}
