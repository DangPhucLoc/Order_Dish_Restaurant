package com.SWD.Order_Dish.controller;

import com.SWD.Order_Dish.model._commonresponse.ResponseDTO;
import com.SWD.Order_Dish.model.orderdetail.OrderDetailRequest;
import com.SWD.Order_Dish.model.orderdetail.OrderDetailResponse;
import com.SWD.Order_Dish.service.OrderDetailService;
import com.SWD.Order_Dish.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orderDetail")
@RequiredArgsConstructor
@Validated
@CrossOrigin
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        List<OrderDetailResponse> result = orderDetailService.getAll();
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "get all orderDetail successfully");
    }
    @GetMapping("/account/{accountId}")
    public ResponseEntity<ResponseDTO> getByAccountId(@PathVariable String accountId) {
        List<OrderDetailResponse> result = orderDetailService.getByAccountId(accountId);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "get all orderDetail by account successfully");
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseDTO> getByOrderId(@PathVariable String orderId) {
        List<OrderDetailResponse> result = orderDetailService.getByOrderId(orderId);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "get all orderDetail by order successfully");
    }

    @GetMapping("/{orderDetailId}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable String orderDetailId) {
        OrderDetailResponse result = orderDetailService.getById(orderDetailId);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "get orderDetail by id successfully");
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO> create(@RequestBody OrderDetailRequest orderDetailRequest) {
        OrderDetailResponse result = orderDetailService.save(orderDetailRequest);
        if(result == null){
            return ResponseUtil.getObject(null,
                    HttpStatus.BAD_REQUEST,
                    "create orderDetail failed");
        }
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "create orderDetail successfully");
    }

    @DeleteMapping("/{orderDetailId}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable String orderDetailId) {
        orderDetailService.deleteOrderDetail(orderDetailId);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "delete orderDetail successfully");
    }
    @PutMapping("/{orderDetailId}/{status}")
    public ResponseEntity<ResponseDTO> updateStatus(@PathVariable String orderDetailId, @PathVariable String status) {
        OrderDetailResponse result = orderDetailService.updateStatus(orderDetailId, status);
        if(result == null){
            return ResponseUtil.getObject(null,
                    HttpStatus.BAD_REQUEST,
                    "update orderDetail status failed");
        }
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "update orderDetail status successfully");
    }


}
