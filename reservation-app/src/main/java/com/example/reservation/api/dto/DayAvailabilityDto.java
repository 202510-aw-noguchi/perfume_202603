package com.example.reservation.api.dto;

import java.util.List;

public record DayAvailabilityDto(
        String date,
        boolean bookable,
        List<SlotAvailabilityDto> slots
) {
}
