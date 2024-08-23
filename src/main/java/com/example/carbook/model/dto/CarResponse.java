package com.example.carbook.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CarResponse(
        Long id,
        String stateNumber,
        String vinCode,
        String make,
        String model,
        String color,
        Long userId
) {
}
