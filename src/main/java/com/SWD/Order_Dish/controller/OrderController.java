package com.SWD.Order_Dish.controller;

import com.SWD.Order_Dish.model._commonresponse.ResponseDTO;
import com.SWD.Order_Dish.model.order.OrderRequest;
import com.SWD.Order_Dish.model.order.OrderResponse;
import com.SWD.Order_Dish.service.OrderService;
import com.SWD.Order_Dish.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Validated
@CrossOrigin
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        List<OrderResponse> result = orderService.findAll();
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Objects fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable String id) {
        OrderResponse result = orderService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<ResponseDTO> getAllByUserId(@PathVariable String userId) {
        List<OrderResponse> result = orderService.findByUserId(userId);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Objects fetched successfully");
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> update(@Valid @RequestBody OrderRequest request) {
        OrderResponse result = orderService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@Valid @RequestBody OrderRequest request) {
        OrderResponse result = orderService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable String id) {
        orderService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}
