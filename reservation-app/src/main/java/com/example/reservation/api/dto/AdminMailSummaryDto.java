package com.example.reservation.api.dto;

import java.util.List;

public record AdminMailSummaryDto(
        String today,
        List<AdminMailDailyTrendDto> monthlyTrend
) {
}
