package com.example.reservation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "slot_capacity")
public class SlotCapacity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate reserveDate;

    @Column(nullable = false, length = 20)
    private String reserveTime;

    @Column(nullable = false)
    private int capacity;

    public LocalDate getReserveDate() {
        return reserveDate;
    }

    public String getReserveTime() {
        return reserveTime;
    }

    public int getCapacity() {
        return capacity;
    }
}
