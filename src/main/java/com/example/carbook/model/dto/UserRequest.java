package com.example.carbook.model.dto;

import com.example.carbook.model.entity.AccessRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotNull
        AccessRole role,
        @Email
        String email,
        @NotBlank
        @Size(min = 8, max = 20)
        String password,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        String patronymic
) {
}
