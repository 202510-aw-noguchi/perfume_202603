package com.example.reservation.repository;

import com.example.reservation.model.SlotCapacity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface SlotCapacityRepository extends JpaRepository<SlotCapacity, Long> {
    Optional<SlotCapacity> findByReserveDateAndReserveTime(LocalDate reserveDate, String reserveTime);
}
