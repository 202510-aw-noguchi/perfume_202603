package com.example.reservation.api.dto;

public record AdminMailSubscriptionDto(
        String email,
        String status,
        String createdAt,
        String updatedAt,
        String unsubscribedAt
) {
}
