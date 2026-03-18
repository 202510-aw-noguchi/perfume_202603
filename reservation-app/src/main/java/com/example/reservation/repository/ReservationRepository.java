package com.example.reservation.repository;

import com.example.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
        select coalesce(sum(r.participantCount), 0)
        from Reservation r
        where r.course = :course
          and r.reserveDate = :reserveDate
          and r.reserveTime = :reserveTime
        """)
    int sumParticipants(
            @Param("course") String course,
            @Param("reserveDate") LocalDate reserveDate,
            @Param("reserveTime") String reserveTime
    );

    List<Reservation> findTop200ByOrderByCreatedAtDesc();
    List<Reservation> findByReserveDateBetween(LocalDate from, LocalDate to);
    List<Reservation> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
