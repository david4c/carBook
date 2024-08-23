package com.example.carbook.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "car")
@Getter
@Setter
@Inheritance
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Car extends BaseEntity {

    @Column(name = "state_number", nullable = false, unique = true)
    private String stateNumber;
    @Column(name = "vin_code", nullable = false, unique = true)
    private String vinCode;
    @Column(name = "make", nullable = false)
    private String make;
    @Column(name = "model", nullable = false)
    private String model;
    @Column(name = "color", nullable = false)
    private String color;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();

}
