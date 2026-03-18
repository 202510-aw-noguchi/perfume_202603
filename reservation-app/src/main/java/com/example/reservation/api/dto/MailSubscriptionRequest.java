package com.example.reservation.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MailSubscriptionRequest(
        @NotBlank @Email String email
) {
}
