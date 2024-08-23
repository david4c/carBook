package com.example.carbook.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "maintenance_record")
@Getter
@Setter
@Inheritance
@SuperBuilder
@NoArgsConstructor
public class MaintenanceRecord extends BaseEntity {
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "date", nullable = false)
    private Instant date;
    private BigDecimal cost;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;
}
