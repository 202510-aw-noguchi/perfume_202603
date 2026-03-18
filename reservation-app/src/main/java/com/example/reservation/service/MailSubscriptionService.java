package com.example.reservation.service;

import com.example.reservation.api.dto.AdminMailDailyTrendDto;
import com.example.reservation.api.dto.AdminMailSummaryDto;
import com.example.reservation.api.dto.AdminMailSubscriptionDto;
import com.example.reservation.api.dto.MailSubscriptionResponse;
import com.example.reservation.model.MailSubscription;
import com.example.reservation.repository.MailSubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class MailSubscriptionService {

    private final MailSubscriptionRepository mailSubscriptionRepository;

    public MailSubscriptionService(MailSubscriptionRepository mailSubscriptionRepository) {
        this.mailSubscriptionRepository = mailSubscriptionRepository;
    }

    @Transactional
    public MailSubscriptionResponse register(String rawEmail) {
        String email = normalizeEmail(rawEmail);
        MailSubscription subscription = mailSubscriptionRepository.findByEmail(email).orElse(null);
        if (subscription == null) {
            MailSubscription created = new MailSubscription();
            created.setEmail(email);
            created.setActive(true);
            created.setUnsubscribedAt(null);
            mailSubscriptionRepository.save(created);
            return new MailSubscriptionResponse("REGISTERED", "登録が完了しました。", email);
        }
        if (subscription.isActive()) {
            return new MailSubscriptionResponse("ALREADY_REGISTERED", "すでに登録済みです。", email);
        }
        subscription.setActive(true);
        subscription.setUnsubscribedAt(null);
        mailSubscriptionRepository.save(subscription);
        return new MailSubscriptionResponse("REGISTERED_AGAIN", "再登録が完了しました。", email);
    }

    @Transactional
    public MailSubscriptionResponse unsubscribe(String rawEmail) {
        String email = normalizeEmail(rawEmail);
        MailSubscription subscription = mailSubscriptionRepository.findByEmail(email).orElse(null);
        if (subscription == null) {
            return new MailSubscriptionResponse("NOT_FOUND", "未登録のメールアドレスです。", email);
        }
        if (!subscription.isActive()) {
            return new MailSubscriptionResponse("ALREADY_UNSUBSCRIBED", "すでに配信停止済みです。", email);
        }
        subscription.setActive(false);
        subscription.setUnsubscribedAt(LocalDateTime.now());
        mailSubscriptionRepository.save(subscription);
        return new MailSubscriptionResponse("UNSUBSCRIBED", "配信停止を受け付けました。", email);
    }

    @Transactional(readOnly = true)
    public List<AdminMailSubscriptionDto> getRecentSubscriptions() {
        return mailSubscriptionRepository.findTop200ByOrderByUpdatedAtDesc().stream()
                .map(s -> new AdminMailSubscriptionDto(
                        s.getEmail(),
                        s.isActive() ? "ACTIVE" : "UNSUBSCRIBED",
                        format(s.getCreatedAt()),
                        format(s.getUpdatedAt()),
                        format(s.getUnsubscribedAt())
                ))
                .toList();
    }

    @Transactional(readOnly = true)
    public AdminMailSummaryDto getAdminMailSummary() {
        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDateTime from = monthStart.atStartOfDay();
        LocalDateTime to = today.plusDays(1).atStartOfDay().minusNanos(1);

        List<MailSubscription> registered = mailSubscriptionRepository.findByCreatedAtBetween(from, to);
        List<MailSubscription> unsubscribed = mailSubscriptionRepository.findByUnsubscribedAtBetween(from, to);

        Map<LocalDate, Integer> registerMap = new HashMap<>();
        for (MailSubscription item : registered) {
            LocalDate date = item.getCreatedAt().toLocalDate();
            registerMap.put(date, registerMap.getOrDefault(date, 0) + 1);
        }

        Map<LocalDate, Integer> unsubscribeMap = new HashMap<>();
        for (MailSubscription item : unsubscribed) {
            if (item.getUnsubscribedAt() == null) {
                continue;
            }
            LocalDate date = item.getUnsubscribedAt().toLocalDate();
            unsubscribeMap.put(date, unsubscribeMap.getOrDefault(date, 0) + 1);
        }

        List<AdminMailDailyTrendDto> monthlyTrend = new ArrayList<>();
        for (LocalDate day = monthStart; !day.isAfter(today); day = day.plusDays(1)) {
            monthlyTrend.add(new AdminMailDailyTrendDto(
                    day.toString(),
                    registerMap.getOrDefault(day, 0),
                    unsubscribeMap.getOrDefault(day, 0)
            ));
        }
        return new AdminMailSummaryDto(today.toString(), monthlyTrend);
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private String format(LocalDateTime value) {
        return value == null ? "" : value.toString();
    }
}
