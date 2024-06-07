package com.SWD.Order_Dish.service;

import com.SWD.Order_Dish.entity.AreaEntity;
import com.SWD.Order_Dish.entity.TableEntity;
import com.SWD.Order_Dish.exception.CustomValidationException;
import com.SWD.Order_Dish.model.area.AreaRequest;
import com.SWD.Order_Dish.model.area.AreaResponse;
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
public class AreaService {

    private final Logger LOGGER = LoggerFactory.getLogger(AreaService.class);
    private final AreaRepository areaRepository;

    public List<AreaResponse> findAll() {
        LOGGER.info("Find all areas");
        List<AreaEntity> areas = areaRepository.findAll();
        if (areas.isEmpty()) {
            LOGGER.warn("No area was found!");
        }
        return areas.stream()
                .map(this::areaResponseGenerator)
                .collect(Collectors.toList());
    }

    public AreaResponse findById(String id) {
        LOGGER.info("Find area with id " + id);
        Optional<AreaEntity> area = areaRepository.findById(id);
        if (area.isEmpty()) {
            LOGGER.warn("No area was found!");
            return null;
        }
        return area.map(this::areaResponseGenerator).get();
    }

    public AreaResponse save(AreaRequest areaRequest) {
        AreaEntity area;

        if (areaRequest.getAreaId() != null) {
            LOGGER.info("Update area with id " + areaRequest.getAreaId());
            checkExist(areaRequest.getAreaId());
            area = areaRepository.findById(areaRequest.getAreaId()).get();
            updateArea(area, areaRequest);
            areaRepository.save(area);
        } else {
            LOGGER.info("Create new area");
            area = createArea(areaRequest);
            areaRepository.save(area);
        }
        return areaResponseGenerator(area);
    }

    public void delete(String id) {
        if (id != null && !id.trim().isEmpty()) {
            LOGGER.info("Delete area with id " + id);
            checkExist(id);
            AreaEntity area = areaRepository.findById(id).get();
            areaRepository.delete(area);
        }
    }

    private AreaEntity createArea(AreaRequest request) {
        AreaEntity area = new AreaEntity();
        updateArea(area, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        area.setCreatedBy(authentication.getName());
        area.setCreatedDate(new Date());
        return area;
    }

    private void updateArea(AreaEntity area, AreaRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        area.setName(request.getName());
        area.setIsAvailable(request.getIsAvailable());
        area.setUpdatedBy(authentication.getName());
        area.setUpdatedDate(new Date());
    }

    private AreaResponse areaResponseGenerator(AreaEntity area) {
        AreaResponse areaResponse = new AreaResponse();
        areaResponse.setAreaId(area.getAreaId());
        areaResponse.setName(area.getName());
        areaResponse.setIsAvailable(area.getIsAvailable());
        areaResponse.setCreatedDate(area.getCreatedDate());
        areaResponse.setCreatedBy(area.getCreatedBy());
        areaResponse.setUpdatedDate(area.getUpdatedDate());
        areaResponse.setUpdatedBy(area.getUpdatedBy());
        areaResponse.setTableIdList(
                area.getTableEntities()
                        .stream()
                        .map(TableEntity::getTableId)
                        .collect(Collectors.toList()));
        return areaResponse;
    }

    private void checkExist(String id) {
        if (areaRepository.findById(id).isEmpty()) {
            LOGGER.error("No area was found!");
            throw new CustomValidationException(List.of("No area was found!"));
        }
    }
}
