package com.SWD.Order_Dish.controller;

import com.SWD.Order_Dish.model._commonresponse.ResponseDTO;
import com.SWD.Order_Dish.model.table.TableRequest;
import com.SWD.Order_Dish.model.table.TableResponse;
import com.SWD.Order_Dish.service.TableService;
import com.SWD.Order_Dish.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/table")
@RequiredArgsConstructor
@Validated
public class TableController {
    private final TableService tableService;

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        List<TableResponse> result = tableService.findAll();
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable String id) {
        TableResponse result = tableService.findById(id);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object fetched successfully");
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> update(@Valid @RequestBody TableRequest request) {
        TableResponse result = tableService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.OK,
                "Object updated successfully");
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> create(@Valid @RequestBody TableRequest request) {
        TableResponse result = tableService.save(request);
        return ResponseUtil.getObject(result,
                HttpStatus.CREATED,
                "Object created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> delete(@PathVariable String id) {
        tableService.delete(id);
        return ResponseUtil.getObject(null,
                HttpStatus.OK,
                "Object deleted successfully");
    }
}
