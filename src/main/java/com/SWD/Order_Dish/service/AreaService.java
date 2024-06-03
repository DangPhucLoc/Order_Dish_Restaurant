package com.SWD.Order_Dish.service;

import com.SWD.Order_Dish.entity.AreaEntity;
import com.SWD.Order_Dish.entity.TableEntity;
import com.SWD.Order_Dish.model.area.AreaRequest;
import com.SWD.Order_Dish.model.area.AreaResponse;
import com.SWD.Order_Dish.repository.AreaRepository;
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
public class AreaService {

    private final AreaRepository areaRepository;

    public List<AreaResponse> findAll() {
        List<AreaEntity> areas = areaRepository.findAll();
        return !areas.isEmpty() ?
                areas.stream()
                        .map(this::areaResponseGenerator)
                        .collect(Collectors.toList())
                : null;
    }

    public AreaResponse findById(String id) {
        Optional<AreaEntity> area = areaRepository.findById(id);
        return area.map(this::areaResponseGenerator).orElse(null);
    }

    public AreaResponse save(AreaRequest areaRequest) {
        AreaEntity area;
        if (areaRequest.getAreaId() != null) {
            area = areaRepository.findById(areaRequest.getAreaId()).get();
            updateArea(area, areaRequest);
            areaRepository.save(area);
        } else {
            area = createArea(areaRequest);
            areaRepository.save(area);
        }
        return areaResponseGenerator(area);
    }

    public void delete(String id) {
        if (id != null && !id.trim().isEmpty()) {
            AreaEntity area = areaRepository.findById(id).get();
            areaRepository.delete(area);
        }
    }

    private AreaEntity createArea(AreaRequest request) {
        AreaEntity area = new AreaEntity();
        setCommonFields(area, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        area.setCreatedBy(authentication.getName());
        area.setCreatedDate(new Date());
        return area;
    }

    private void updateArea(AreaEntity area, AreaRequest request) {
        setCommonFields(area, request);
    }

    private void setCommonFields(AreaEntity area, AreaRequest request) {
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
}
