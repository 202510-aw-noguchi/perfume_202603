package com.example.reservation.api.dto;

public record SlotAvailabilityDto(
        String time,
        int remaining,
        String status,
        boolean bookable
) {
}
