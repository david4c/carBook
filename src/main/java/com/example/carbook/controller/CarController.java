package com.example.carbook.controller;

import com.example.carbook.config.security.dto.CustomUserDetails;
import com.example.carbook.model.dto.CarRequest;
import com.example.carbook.model.dto.CarResponse;
import com.example.carbook.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/car")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_USER')")
@Validated
public class CarController {

    private final CarService carService;

    @GetMapping
    public List<EntityModel<CarResponse>> getAll(@AuthenticationPrincipal final CustomUserDetails userDetails) {
        log.info("Fetching all cars for user: {}", userDetails.getUsername());
        return carService.findAllByUser(userDetails.getUser())
                .stream()
                .map(carDTO -> toEntityModel(userDetails, carDTO))
                .toList();
    }

    @GetMapping("{id}")
    public EntityModel<CarResponse> getById(@AuthenticationPrincipal final CustomUserDetails userDetails, @PathVariable final Long id) {
        log.info("Fetching car with id {} for user: {}", id, userDetails.getUsername());
        return toEntityModel(userDetails, carService.findByIdForUser(userDetails.getUser(), id));
    }

    @PostMapping
    public EntityModel<CarResponse> createCar(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @RequestBody @Valid final CarRequest carRequest) {
        log.info("Creating car for user: {}", userDetails.getUsername());
        final CarResponse savedCar = carService.saveWithUser(userDetails.getUser(), carRequest);
        return toEntityModel(userDetails, savedCar);
    }

    @PutMapping("{id}")
    public EntityModel<CarResponse> update(
            @AuthenticationPrincipal final CustomUserDetails userDetails,
            @PathVariable final Long id,
            @RequestBody @Valid final CarRequest carRequest
    ) {
        log.info("Updating car with id {} for user: {}", id, userDetails.getUsername());
        final CarResponse savedCar = carService.updateForUser(userDetails.getUser(), id, carRequest);
        return toEntityModel(userDetails, savedCar);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal final CustomUserDetails userDetails, @PathVariable final Long id) {
        log.info("Deleting car with id {} for user: {}", id, userDetails.getUsername());
        carService.deleteByIdForUser(userDetails.getUser(), id);
    }

    private EntityModel<CarResponse> toEntityModel(final CustomUserDetails userDetails, final CarResponse carResponse) {
        EntityModel<CarResponse> model = EntityModel.of(carResponse);
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CarController.class)
                .getById(userDetails, carResponse.id())).withSelfRel());
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CarController.class)
                .getAll(userDetails)).withRel("all-records"));
        return model;
    }
}
