package com.example.carbook.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Inheritance;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "users")
@Getter
@Setter
@Inheritance
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Enumerated(STRING)
    @Column(name = "role", nullable = false)
    private AccessRole role;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "is_enabled", nullable = false)
    private boolean enabled;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(name = "patronymic")
    private String patronymic;

    public String getFullName() {
        return lastName + " " + firstName + (patronymic != null ? " " + patronymic : "");
    }
}
