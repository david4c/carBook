package com.example.carbook.model.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
public record MaintenanceRecordResponse(
        Long id,
        String description,
        Instant date,
        BigDecimal cost,
        CarResponse car
) {
}
