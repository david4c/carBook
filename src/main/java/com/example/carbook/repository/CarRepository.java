package com.example.carbook.repository;

import com.example.carbook.model.entity.Car;
import com.example.carbook.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByUser(User user);

    Optional<Car> findByUserAndId(User user, Long id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Car c WHERE c.id = :carId AND c.user = :user")
    boolean existsByIdAndUser(@Param("carId") Long carId, @Param("user") User user);

}
