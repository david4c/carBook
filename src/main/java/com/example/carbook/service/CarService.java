package com.example.carbook.service;

import com.example.carbook.exception.CarNotFoundException;
import com.example.carbook.mapper.CarRequestMapper;
import com.example.carbook.mapper.CarResponseMapper;
import com.example.carbook.model.dto.CarRequest;
import com.example.carbook.model.dto.CarResponse;
import com.example.carbook.model.entity.Car;
import com.example.carbook.model.entity.User;
import com.example.carbook.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarService implements CRUDService<Car, Long> {

    public static final String CAR_NOT_FOUND = "Car not found!";

    private final CarRepository carRepository;
    private final CarRequestMapper carRequestMapper;
    private final CarResponseMapper carResponseMapper;


    @Override
    public Car findById(final Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(CAR_NOT_FOUND));

    }

    public CarResponse findByIdForUser(final User user, final Long id) {
        return carResponseMapper.carToCarDto(carRepository.findByUserAndId(user, id)
                .orElseThrow(() -> new CarNotFoundException(CAR_NOT_FOUND)));
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }


    public List<CarResponse> findAllByUser(final User user) {
        return carResponseMapper.carListToCarDtoList(carRepository.findByUser(user));
    }


    @Override
    public Car save(final Car carForSave) {
        return carRepository.save(carForSave);
    }

    public CarResponse saveWithUser(final User user, CarRequest carRequest) {
        final Car carForSave = carRequestMapper.carDtoToCar(carRequest);
        carForSave.setUser(user);
        final Car savedCar = carRepository.save(carForSave);
        return carResponseMapper.carToCarDto(savedCar);
    }

    @Override
    public void deleteById(final Long id) {
        carRepository.deleteById(id);
    }

    public void deleteByIdForUser(final User user, final Long id) {
        if (!userOwnsCar(user, id)) {
            denyAccess();
        }
        deleteById(id);
    }

    @Override
    public Car update(final Long id, final Car carForUpdate) {
        return carRepository.save(carForUpdate);
    }

    public CarResponse updateForUser(final User user, final Long id, final CarRequest carRequest) {
        final Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new AccessDeniedException(CAR_NOT_FOUND));

        if (!userOwnsCar(user, id)) {
            denyAccess();
        }
        existingCar.setUser(user);
        existingCar.setMake(carRequest.make());
        existingCar.setModel(carRequest.model());
        existingCar.setColor(carRequest.color());
        existingCar.setVinCode(carRequest.vinCode());
        existingCar.setStateNumber(carRequest.stateNumber());
        final Car savedCar = carRepository.save(existingCar);
        return carResponseMapper.carToCarDto(savedCar);

    }

    public boolean userOwnsCar(final User user, final Long carId) {
        return carRepository.existsByIdAndUser(carId, user);
    }

    private void denyAccess() {
        throw new AccessDeniedException("User does not own car");
    }
}
