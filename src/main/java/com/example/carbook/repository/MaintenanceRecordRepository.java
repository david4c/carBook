package com.example.carbook.repository;

import com.example.carbook.model.entity.MaintenanceRecord;
import com.example.carbook.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MaintenanceRecordRepository extends JpaRepository<MaintenanceRecord, Long> {

    Page<MaintenanceRecord> findByCarUser(User user, Pageable pageable);

    Optional<MaintenanceRecord> findByCarUserAndId(User user, Long id);

    Page<MaintenanceRecord> findAllByCarId(Long carId, Pageable pageable);

    @Query("SELECT m FROM MaintenanceRecord m WHERE m.description LIKE %:description%")
    Page<MaintenanceRecord> findByDescriptionContaining(@Param("description") String description, Pageable pageable);
}
