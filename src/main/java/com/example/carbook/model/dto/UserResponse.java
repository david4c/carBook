package com.example.carbook.model.dto;

import com.example.carbook.model.entity.AccessRole;

public record UserResponse(
        Long id,
        AccessRole role,
        String email,
        String firstName,
        String lastName,
        String patronymic
) {
}
