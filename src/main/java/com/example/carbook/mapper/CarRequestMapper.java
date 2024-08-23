package com.example.carbook.mapper;

import com.example.carbook.config.MapstructConfig;
import com.example.carbook.model.dto.CarRequest;
import com.example.carbook.model.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapstructConfig.class)
public abstract class CarRequestMapper {

    @Mapping(source = "user.id", target = "userId")
    public abstract CarRequest carToCarDto(Car car);

    @Mapping(source = "userId", target = "user.id")
    public abstract Car carDtoToCar(CarRequest carRequest);

    public abstract List<CarRequest> carListToCarDtoList(List<Car> carList);
}
