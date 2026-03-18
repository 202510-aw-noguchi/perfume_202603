package com.example.reservation.api.dto;

public record AdminReservationDto(
        String reservationNumber,
        String course,
        String reserveDate,
        String reserveTime,
        int participantCount,
        String contactName,
        String contactPhone,
        String contactEmail,
        String createdAt
) {
}
