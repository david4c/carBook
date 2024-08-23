package com.example.carbook.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public enum AccessRole implements GrantedAuthority {
    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
