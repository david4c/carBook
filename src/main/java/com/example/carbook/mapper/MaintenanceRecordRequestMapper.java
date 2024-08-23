package com.example.carbook.mapper;

import com.example.carbook.config.MapstructConfig;
import com.example.carbook.model.dto.MaintenanceRecordRequest;
import com.example.carbook.model.entity.MaintenanceRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapstructConfig.class)
public abstract class MaintenanceRecordRequestMapper {

    @Mapping(source = "carId", target = "car.id")
    public abstract MaintenanceRecord dtoToEntity(MaintenanceRecordRequest dto);

    @Mapping(source = "car.id", target = "carId")
    public abstract MaintenanceRecordRequest entityToDto(MaintenanceRecord maintenanceRecord);

    public abstract List<MaintenanceRecordRequest> entityListToDtoList(List<MaintenanceRecord> maintenanceRecords);
}
