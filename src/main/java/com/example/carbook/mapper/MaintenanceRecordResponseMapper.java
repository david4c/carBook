package com.example.carbook.mapper;

import com.example.carbook.config.MapstructConfig;
import com.example.carbook.model.dto.MaintenanceRecordResponse;
import com.example.carbook.model.entity.MaintenanceRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapstructConfig.class, uses = CarResponseMapper.class)
public abstract class MaintenanceRecordResponseMapper {

    @Mapping(source = "car", target = "car")
    public abstract MaintenanceRecordResponse entityToDto(MaintenanceRecord maintenanceRecord);

    public abstract List<MaintenanceRecordResponse> entityListToDtoList(List<MaintenanceRecord> all);
}

