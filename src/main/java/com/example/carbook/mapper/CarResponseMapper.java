package com.example.carbook.mapper;

import com.example.carbook.config.MapstructConfig;
import com.example.carbook.model.dto.CarResponse;
import com.example.carbook.model.entity.Car;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = MapstructConfig.class)
public abstract class CarResponseMapper {

    public abstract CarResponse carToCarDto(Car car);

    public abstract List<CarResponse> carListToCarDtoList(List<Car> all);
}
