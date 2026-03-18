package com.example.reservation.api.dto;

import java.util.List;

public record AdminWeeklyReservationTableDto(
        List<String> dates,
        List<AdminWeeklySlotRowDto> rows
) {
}
