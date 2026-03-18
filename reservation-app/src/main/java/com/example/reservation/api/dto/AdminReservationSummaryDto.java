package com.example.reservation.api.dto;

import java.util.List;

public record AdminReservationSummaryDto(
        String today,
        AdminWeeklyReservationTableDto weeklyReservationStatus,
        List<AdminDailyCountDto> monthlyReceivedTrend
) {
}
