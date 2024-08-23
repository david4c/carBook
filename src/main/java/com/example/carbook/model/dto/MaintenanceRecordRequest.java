package com.example.carbook.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record MaintenanceRecordRequest(
        Long id,
        @NotBlank
        String description,
        @NotNull
        Instant date,
        @NotNull
        @Positive
        BigDecimal cost,
        @NotNull
        @Positive
        Long carId
) {
}
