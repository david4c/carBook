package com.example.carbook.service;

import com.example.carbook.exception.MaintenanceRecordNotFoundException;
import com.example.carbook.mapper.MaintenanceRecordRequestMapper;
import com.example.carbook.mapper.MaintenanceRecordResponseMapper;
import com.example.carbook.model.dto.CarResponse;
import com.example.carbook.model.dto.MaintenanceRecordRequest;
import com.example.carbook.model.dto.MaintenanceRecordResponse;
import com.example.carbook.model.entity.MaintenanceRecord;
import com.example.carbook.model.entity.User;
import com.example.carbook.repository.MaintenanceRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceRecordService implements CRUDService<MaintenanceRecord, Long> {

    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final MaintenanceRecordRequestMapper maintenanceRecordRequestMapper;
    private final MaintenanceRecordResponseMapper maintenanceRecordResponseMapper;
    private final CarService carService;

    @Override
    public MaintenanceRecord findById(final Long id) {
        return maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new MaintenanceRecordNotFoundException("Maintenance Record not found"));

    }

    @Override
    public List<MaintenanceRecord> findAll() {
        return maintenanceRecordRepository.findAll();
    }

    @Override
    public MaintenanceRecord save(final MaintenanceRecord recordForSave) {
        return maintenanceRecordRepository.save(recordForSave);
    }

    public MaintenanceRecordResponse saveWithUser(User user, MaintenanceRecordRequest maintenanceRecordRequest) {
        if (lacksAccessToCar(user, maintenanceRecordRequest.carId())) {
            denyAccess();
        }
        final MaintenanceRecord recordForSave = maintenanceRecordRequestMapper
                .dtoToEntity(maintenanceRecordRequest);
        final MaintenanceRecord savedRecord = maintenanceRecordRepository.save(recordForSave);
        return maintenanceRecordResponseMapper.entityToDto(savedRecord);
    }

    @Override
    public void deleteById(final Long id) {
        maintenanceRecordRepository.deleteById(id);
    }

    @Override
    public MaintenanceRecord update(final Long id, final MaintenanceRecord recordForUpdate) {
        final MaintenanceRecord existRecord = findById(id);
        existRecord.setDescription(recordForUpdate.getDescription());
        existRecord.setDate(recordForUpdate.getDate());
        existRecord.setCost(recordForUpdate.getCost());

        return maintenanceRecordRepository.save(existRecord);
    }

    public MaintenanceRecordResponse findByIdForUser(User user, Long id) {
        return maintenanceRecordResponseMapper.entityToDto(
                maintenanceRecordRepository.findByCarUserAndId(user, id)
                        .orElseThrow(() -> new MaintenanceRecordNotFoundException("Maintenance Record not found")));
    }

    public Map<CarResponse, List<MaintenanceRecordResponse>> findAllByUserGroupedByCar(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MaintenanceRecord> recordsPage = maintenanceRecordRepository.findByCarUser(user, pageable);

        return recordsPage.getContent().stream()
                .map(maintenanceRecordResponseMapper::entityToDto)
                .collect(Collectors.groupingBy(MaintenanceRecordResponse::car));
    }

    public Page<MaintenanceRecordResponse> getRecordsByCarId(User user, Long carId, Pageable pageable) {
        if (lacksAccessToCar(user, carId)) {
            denyAccess();
        }
        Page<MaintenanceRecord> maintenanceRecordsPage = maintenanceRecordRepository.findAllByCarId(carId, pageable);
        return maintenanceRecordsPage.map(maintenanceRecordResponseMapper::entityToDto);
    }

    public MaintenanceRecordResponse updateForUser(
            final User user, final Long id, final MaintenanceRecordRequest maintenanceRecordRequest
    ) {
        final MaintenanceRecord existRecord = findById(id);
        if (lacksAccessToCar(user, existRecord.getCar().getId())) {
            denyAccess();
        }
        existRecord.setDescription(maintenanceRecordRequest.description());
        existRecord.setDate(maintenanceRecordRequest.date());
        existRecord.setCost(maintenanceRecordRequest.cost());
        final MaintenanceRecord recordForUpdate = save(existRecord);
        final MaintenanceRecord updatedRecord = maintenanceRecordRepository.save(recordForUpdate);
        return maintenanceRecordResponseMapper.entityToDto(updatedRecord);
    }

    public Page<MaintenanceRecordResponse> searchByDescription(String description, Pageable pageable) {
        return maintenanceRecordRepository.findByDescriptionContaining(description, pageable)
                .map(maintenanceRecordResponseMapper::entityToDto);
    }

    private void denyAccess() {
        throw new AccessDeniedException("User does not own car");
    }

    private boolean lacksAccessToCar(final User user, final Long id) {
        return carService.userOwnsCar(user, id);
    }
}
