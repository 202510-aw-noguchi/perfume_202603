package com.example.reservation.api;

import com.example.reservation.api.dto.AdminMailSubscriptionDto;
import com.example.reservation.api.dto.AdminMailSummaryDto;
import com.example.reservation.api.dto.AdminMeDto;
import com.example.reservation.api.dto.AdminReservationDto;
import com.example.reservation.api.dto.AdminReservationSummaryDto;
import com.example.reservation.service.MailSubscriptionService;
import com.example.reservation.service.ReservationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ReservationService reservationService;
    private final MailSubscriptionService mailSubscriptionService;

    public AdminController(ReservationService reservationService, MailSubscriptionService mailSubscriptionService) {
        this.reservationService = reservationService;
        this.mailSubscriptionService = mailSubscriptionService;
    }

    @GetMapping("/reservations")
    public List<AdminReservationDto> reservations() {
        return reservationService.getRecentReservations();
    }

    @GetMapping("/reservation-summary")
    public AdminReservationSummaryDto reservationSummary() {
        return reservationService.getAdminSummary();
    }

    @GetMapping("/mail-subscriptions")
    public List<AdminMailSubscriptionDto> mailSubscriptions() {
        return mailSubscriptionService.getRecentSubscriptions();
    }

    @GetMapping("/mail-summary")
    public AdminMailSummaryDto mailSummary() {
        return mailSubscriptionService.getAdminMailSummary();
    }

    @GetMapping("/me")
    public AdminMeDto me(Authentication authentication) {
        return new AdminMeDto(authentication == null ? "" : authentication.getName());
    }
}
