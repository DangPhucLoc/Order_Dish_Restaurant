package com.SWD.Order_Dish.controller;

import com.SWD.Order_Dish.model._commonresponse.ResponseDTO;
import com.SWD.Order_Dish.model.area.AreaRequest;
import com.SWD.Order_Dish.model.area.AreaResponse;
import com.SWD.Order_Dish.service.AreaService;
import com.SWD.Order_Dish.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/area")
@RequiredArgsConstructor
@Validated
@CrossOrigin
public class AreaController {
    private final AreaService areaService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        List<AreaResponse> result = areaService.findAll();
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable String id) {
        AreaResponse result = areaService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> update(@Valid @RequestBody AreaRequest request) {
        AreaResponse result = areaService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@Valid @RequestBody AreaRequest request) {
        AreaResponse result = areaService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable String id) {
        areaService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}
