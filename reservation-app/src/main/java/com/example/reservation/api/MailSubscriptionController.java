package com.example.reservation.api;

import com.example.reservation.api.dto.MailSubscriptionRequest;
import com.example.reservation.api.dto.MailSubscriptionResponse;
import com.example.reservation.service.MailSubscriptionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail/subscriptions")
public class MailSubscriptionController {

    private final MailSubscriptionService mailSubscriptionService;

    public MailSubscriptionController(MailSubscriptionService mailSubscriptionService) {
        this.mailSubscriptionService = mailSubscriptionService;
    }

    @PostMapping("/register")
    public MailSubscriptionResponse register(@Valid @RequestBody MailSubscriptionRequest request) {
        return mailSubscriptionService.register(request.email());
    }

    @PostMapping("/unsubscribe")
    public MailSubscriptionResponse unsubscribe(@Valid @RequestBody MailSubscriptionRequest request) {
        return mailSubscriptionService.unsubscribe(request.email());
    }
}
