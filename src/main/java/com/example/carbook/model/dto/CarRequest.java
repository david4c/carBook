package com.example.carbook.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CarRequest(
        Long id,
        @NotBlank
        String stateNumber,
        @NotBlank
        String vinCode,
        @NotBlank
        String make,
        @NotBlank
        String model,
        @NotBlank
        String color,
        Long userId
) {
}
