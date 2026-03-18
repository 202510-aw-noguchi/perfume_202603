package com.example.reservation.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record ReservationRequest(
        @NotBlank String course,
        @NotNull LocalDate reserveDate,
        @NotBlank String reserveTime,
        @Min(1) @Max(20) int participantCount,
        @NotBlank String contactName,
        @NotBlank @Pattern(regexp = "^[0-9]{10,11}$") String contactPhone,
        @NotBlank @Email String contactEmail
) {
}
