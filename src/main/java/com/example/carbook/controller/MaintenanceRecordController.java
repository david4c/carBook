package com.example.carbook.controller;

import com.example.carbook.config.PaginationProperties;
import com.example.carbook.config.security.dto.CustomUserDetails;
import com.example.carbook.model.dto.CarResponse;
import com.example.carbook.model.dto.MaintenanceRecordRequest;
import com.example.carbook.model.dto.MaintenanceRecordResponse;
import com.example.carbook.model.dto.PaginatedMaintenanceRecordResponse;
import com.example.carbook.service.MaintenanceRecordService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("api/v1/record")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
@Validated
public class MaintenanceRecordController {

    public static final String CAR_RECORDS = "car-records";
    public static final String ALL_RECORDS = "all-records";
    private static final int SEARCH_MAX_STRING_LENGTH = 255;

    private final MaintenanceRecordService maintenanceRecordService;
    private final PaginationProperties paginationProperties;

    @GetMapping("/car/{carId}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<PaginatedMaintenanceRecordResponse> getRecordsByCarId(
            @AuthenticationPrincipal  final CustomUserDetails userDetails,
            @PathVariable Long carId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        log.info("Fetching records for car ID {}", carId);
        Pageable pageable = buildPageable(page, size);
        Page<MaintenanceRecordResponse> resultPage = maintenanceRecordService
                .getRecordsByCarId(userDetails.getUser(), carId, pageable);

        PaginatedMaintenanceRecordResponse paginatedResponse = new PaginatedMaintenanceRecordResponse(
                resultPage.getContent(),
                resultPage.getNumber(),
                resultPage.getTotalPages()
        );
        log.info("Fetched {} records for car ID {}", resultPage.getTotalElements(), carId);
        return EntityModel.of(paginatedResponse,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaintenanceRecordController.class)
                        .getRecordsByCarId(userDetails, carId, pageable.getPageNumber(), pageable.getPageSize())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaintenanceRecordController.class)
                                .getAllRecordsGroupedByCar(
                                        userDetails,
                                        pageable.getPageNumber(),
                                        pageable.getPageSize()))
                        .withRel(ALL_RECORDS));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<CarResponse, List<EntityModel<MaintenanceRecordResponse>>> getAllRecordsGroupedByCar(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Pageable pageable = buildPageable(page, size);
        Map<CarResponse, List<MaintenanceRecordResponse>> groupedRecords = maintenanceRecordService
                .findAllByUserGroupedByCar(userDetails.getUser(), pageable.getPageNumber(), pageable.getPageSize());

        return groupedRecords.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .map(r -> toEntityModel(userDetails, r))
                                .toList()
                ));
    }

    @GetMapping("{id}")
    public EntityModel<MaintenanceRecordResponse> getRecordById(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @PathVariable final Long id
    ) {
        return toEntityModel(userDetails, maintenanceRecordService.findByIdForUser(userDetails.getUser(), id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<MaintenanceRecordResponse> createRecord(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestBody @Valid final MaintenanceRecordRequest maintenanceRecordRequest
    ) {
        log.debug("Creating new maintenance record for user {} with request data: {}", userDetails.getUsername(), maintenanceRecordRequest);
        final MaintenanceRecordResponse savedRecord = maintenanceRecordService
                .saveWithUser(userDetails.getUser(), maintenanceRecordRequest);
        log.info("Created maintenance record with ID {}", savedRecord.id());
        return toEntityModel(userDetails, savedRecord);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<PaginatedMaintenanceRecordResponse> searchRecords(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestParam @Size(max = SEARCH_MAX_STRING_LENGTH) String description,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size
    ) {
        Pageable pageable = buildPageable(page, size);
        Page<MaintenanceRecordResponse> resultPage = maintenanceRecordService
                .searchByDescription(description, pageable);

        PaginatedMaintenanceRecordResponse paginatedResponse = new PaginatedMaintenanceRecordResponse(
                resultPage.getContent(),
                resultPage.getNumber(),
                resultPage.getTotalPages()
        );

        return EntityModel.of(paginatedResponse,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaintenanceRecordController.class)
                        .searchRecords(userDetails, description, pageable.getPageNumber(), pageable.getPageSize())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaintenanceRecordController.class)
                        .getAllRecordsGroupedByCar(userDetails, pageable.getPageNumber(), pageable.getPageSize())).withRel(ALL_RECORDS));
    }


    @PutMapping("{recordId}")
    public EntityModel<MaintenanceRecordResponse> updateRecord(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @PathVariable final Long recordId,
            @RequestBody @Valid final MaintenanceRecordRequest maintenanceRecordRequest
    ) {
        log.debug("Updating maintenance record with ID {} for user {} with request data: {}",
                recordId, userDetails.getUsername(), maintenanceRecordRequest);
        final MaintenanceRecordResponse updatedRecordDTO = maintenanceRecordService
                .updateForUser(userDetails.getUser(), recordId, maintenanceRecordRequest);
        log.info("Updated maintenance record with ID {}", updatedRecordDTO.id());

        return toEntityModel(userDetails, updatedRecordDTO);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecord(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @PathVariable final Long id
    ) {
        log.info("Deleting maintenance record with ID {} for user {}", id, userDetails.getUsername());
        maintenanceRecordService.deleteById(id);
        log.info("Deleted maintenance record with ID {}", id);
    }

    private EntityModel<MaintenanceRecordResponse> toEntityModel(
            final CustomUserDetails userDetails,
            final MaintenanceRecordResponse maintenanceRecord
    ) {
        EntityModel<MaintenanceRecordResponse> model = EntityModel.of(maintenanceRecord);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaintenanceRecordController.class)
                .getRecordById(userDetails, maintenanceRecord.id())).withSelfRel());
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaintenanceRecordController.class)
                        .getRecordsByCarId(
                                userDetails,
                                maintenanceRecord.car().id(),
                                paginationProperties.getDefaultPage(),
                                paginationProperties.getDefaultSize()))
                .withRel(CAR_RECORDS));
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MaintenanceRecordController.class)
                        .getAllRecordsGroupedByCar(
                                userDetails,
                                paginationProperties.getDefaultPage(),
                                paginationProperties.getDefaultSize()))
                .withRel(ALL_RECORDS));
        return model;
    }

    private Pageable buildPageable(Integer page, Integer size) {
        int pageNum = (page != null) ? page : paginationProperties.getDefaultPage();
        int pageSize = (size != null) ? size : paginationProperties.getDefaultSize();
        return PageRequest.of(pageNum, pageSize);
    }
}
