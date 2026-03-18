package com.example.reservation.api.dto;

import java.util.List;

public record AvailabilityResponse(
        String course,
        List<DayAvailabilityDto> days
) {
}
