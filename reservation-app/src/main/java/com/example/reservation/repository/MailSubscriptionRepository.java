package com.example.reservation.repository;

import com.example.reservation.model.MailSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MailSubscriptionRepository extends JpaRepository<MailSubscription, Long> {
    Optional<MailSubscription> findByEmail(String email);
    List<MailSubscription> findTop200ByOrderByUpdatedAtDesc();
    List<MailSubscription> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
    List<MailSubscription> findByUnsubscribedAtBetween(LocalDateTime from, LocalDateTime to);
}
