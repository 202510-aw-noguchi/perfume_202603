package com.example.reservation.api.dto;

import java.util.List;

public record AdminWeeklySlotRowDto(
        String time,
        List<Integer> counts
) {
}
