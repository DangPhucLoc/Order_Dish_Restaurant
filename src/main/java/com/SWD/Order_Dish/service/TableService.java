package com.SWD.Order_Dish.service;

import com.SWD.Order_Dish.entity.AreaEntity;
import com.SWD.Order_Dish.entity.TableEntity;
import com.SWD.Order_Dish.exception.CustomValidationException;
import com.SWD.Order_Dish.model.table.TableRequest;
import com.SWD.Order_Dish.model.table.TableResponse;
import com.SWD.Order_Dish.repository.TableRepository;
import com.SWD.Order_Dish.repository.AreaRepository;
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
public class TableService {

    private final Logger LOGGER = LoggerFactory.getLogger(TableService.class);
    private final TableRepository tableRepository;
    private final AreaRepository areaRepository;

    public List<TableResponse> findAll() {
        LOGGER.info("Find all tables");
        List<TableEntity> tables = tableRepository.findAll();
        if (tables.isEmpty()) {
            LOGGER.warn("No tables were found!");
        }
        return tables.stream()
                .map(this::tableResponseGenerator)
                .collect(Collectors.toList());
    }

    public TableResponse findById(String id) {
        LOGGER.info("Find table with id " + id);
        Optional<TableEntity> table = tableRepository.findById(id);
        if (table.isEmpty()) {
            LOGGER.warn("No table was found!");
            return null;
        }
        return table.map(this::tableResponseGenerator).get();
    }

    public TableResponse save(TableRequest tableRequest) {
        TableEntity table;

        if (tableRequest.getTableId() != null) {
            LOGGER.info("Update table with id " + tableRequest.getTableId());
            checkExist(tableRequest.getTableId());
            table = tableRepository.findById(tableRequest.getTableId()).get();
            updateTable(table, tableRequest);
            tableRepository.save(table);
        } else {
            LOGGER.info("Create new table");
            table = createTable(tableRequest);
            tableRepository.save(table);
        }
        return tableResponseGenerator(table);
    }

    public void delete(String id) {
        if (id != null && !id.trim().isEmpty()) {
            LOGGER.info("Delete table with id " + id);
            checkExist(id);
            TableEntity table = tableRepository.findById(id).get();
            tableRepository.delete(table);
        }
    }

    private TableEntity createTable(TableRequest request) {
        TableEntity table = new TableEntity();
        setCommonFields(table, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        table.setCreatedBy(authentication.getName());
        table.setCreatedDate(new Date());
        AreaEntity area = areaRepository.findById(request.getAreaId()).get();
        table.setAreaEntity(area);
        return table;
    }

    private void updateTable(TableEntity table, TableRequest request) {
        setCommonFields(table, request);
        AreaEntity area = areaRepository.findById(request.getAreaId()).get();
        table.setAreaEntity(area);
    }

    private void setCommonFields(TableEntity table, TableRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        table.setName(request.getName());
        table.setIsAvailable(request.getIsAvailable());
        table.setUpdatedBy(authentication.getName());
        table.setUpdatedDate(new Date());
    }

    private TableResponse tableResponseGenerator(TableEntity table) {
        TableResponse tableResponse = new TableResponse();
        tableResponse.setTableId(table.getTableId());
        tableResponse.setName(table.getName());
        tableResponse.setIsAvailable(table.getIsAvailable());
        tableResponse.setCreatedDate(table.getCreatedDate());
        tableResponse.setCreatedBy(table.getCreatedBy());
        tableResponse.setUpdatedDate(table.getUpdatedDate());
        tableResponse.setUpdatedBy(table.getUpdatedBy());
        tableResponse.setAreaId(table.getAreaEntity().getAreaId());
        return tableResponse;
    }

    private void checkExist(String id) {
        if (tableRepository.findById(id).isEmpty()) {
            LOGGER.error("No table was found!");
            throw new CustomValidationException(List.of("No table was found!"));
        }
    }
}
