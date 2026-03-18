package com.example.reservation.api.dto;

public record MailSubscriptionResponse(
        String result,
        String message,
        String email
) {
}
