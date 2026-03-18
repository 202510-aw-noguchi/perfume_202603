package com.example.reservation.api.dto;

public record AdminMailDailyTrendDto(
        String date,
        int registeredCount,
        int unsubscribedCount
) {
}
