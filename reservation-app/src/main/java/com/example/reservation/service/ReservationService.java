package com.example.reservation.service;

import com.example.reservation.api.dto.AdminDailyCountDto;
import com.example.reservation.api.dto.AdminReservationSummaryDto;
import com.example.reservation.api.dto.AvailabilityResponse;
import com.example.reservation.api.dto.DayAvailabilityDto;
import com.example.reservation.api.dto.AdminReservationDto;
import com.example.reservation.api.dto.AdminWeeklyReservationTableDto;
import com.example.reservation.api.dto.AdminWeeklySlotRowDto;
import com.example.reservation.api.dto.ReservationRequest;
import com.example.reservation.api.dto.SlotAvailabilityDto;
import com.example.reservation.model.Reservation;
import com.example.reservation.model.SlotCapacity;
import com.example.reservation.repository.ReservationRepository;
import com.example.reservation.repository.SlotCapacityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private static final Set<String> COURSES = Set.of("classic", "luxe");
    private static final List<String> SLOTS = List.of("10:00-11:00", "14:00-15:00", "16:00-17:00");
    private static final int DEFAULT_CAPACITY = 20;
    private static final DateTimeFormatter NUMBER_DATE = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.JAPAN);

    private final ReservationRepository reservationRepository;
    private final SlotCapacityRepository slotCapacityRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            SlotCapacityRepository slotCapacityRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.slotCapacityRepository = slotCapacityRepository;
    }

    @Transactional(readOnly = true)
    public AvailabilityResponse getAvailability(String course, LocalDate from, LocalDate to) {
        String normalizedCourse = normalizeCourse(course);
        if (to.isBefore(from)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "to must be on or after from.");
        }
        List<DayAvailabilityDto> days = new ArrayList<>();
        for (LocalDate day = from; !day.isAfter(to); day = day.plusDays(1)) {
            List<SlotAvailabilityDto> slots = new ArrayList<>();
            boolean dayBookable = false;
            for (String slot : SLOTS) {
                SlotAvailabilityDto detail = computeSlotAvailability(normalizedCourse, day, slot);
                slots.add(detail);
                if (detail.bookable()) {
                    dayBookable = true;
                }
            }
            days.add(new DayAvailabilityDto(day.toString(), dayBookable, slots));
        }
        return new AvailabilityResponse(normalizedCourse, days);
    }

    @Transactional
    public String createReservation(ReservationRequest request) {
        String course = normalizeCourse(request.course());
        if (!SLOTS.contains(request.reserveTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reserve_time.");
        }
        if (request.reserveDate().isBefore(LocalDate.now().plusDays(1))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date out of booking range.");
        }

        SlotAvailabilityDto slot = computeSlotAvailability(course, request.reserveDate(), request.reserveTime());
        if (!slot.bookable() || slot.remaining() < request.participantCount()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Selected date/time is full.");
        }

        Reservation reservation = new Reservation();
        String tempNumber = "TMP-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        reservation.setReservationNumber(tempNumber);
        reservation.setCourse(course);
        reservation.setReserveDate(request.reserveDate());
        reservation.setReserveTime(request.reserveTime());
        reservation.setParticipantCount(request.participantCount());
        reservation.setContactName(request.contactName());
        reservation.setContactPhone(request.contactPhone());
        reservation.setContactEmail(request.contactEmail());

        Reservation saved = reservationRepository.save(reservation);
        String reservationNumber = "RSV-" + request.reserveDate().format(NUMBER_DATE) + "-" + String.format("%05d", saved.getId());
        saved.setReservationNumber(reservationNumber);
        reservationRepository.save(saved);
        return reservationNumber;
    }

    @Transactional(readOnly = true)
    public List<AdminReservationDto> getRecentReservations() {
        return reservationRepository.findTop200ByOrderByCreatedAtDesc().stream()
                .map(r -> new AdminReservationDto(
                        r.getReservationNumber(),
                        r.getCourse(),
                        r.getReserveDate().toString(),
                        r.getReserveTime(),
                        r.getParticipantCount(),
                        r.getContactName(),
                        r.getContactPhone(),
                        r.getContactEmail(),
                        r.getCreatedAt() == null ? "" : r.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminReservationSummaryDto getAdminSummary() {
        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = weekStart.plusDays(6);
        List<LocalDate> weekDates = new ArrayList<>();
        for (int i = 0; i < 7; i += 1) {
            weekDates.add(weekStart.plusDays(i));
        }

        List<Reservation> weeklyReservations = reservationRepository.findByReserveDateBetween(weekStart, weekEnd);
        Map<String, Integer> weeklyCountMap = new HashMap<>();
        for (Reservation reservation : weeklyReservations) {
            String key = reservation.getReserveDate() + "|" + reservation.getReserveTime();
            weeklyCountMap.put(key, weeklyCountMap.getOrDefault(key, 0) + 1);
        }

        List<AdminWeeklySlotRowDto> weeklyRows = new ArrayList<>();
        for (String slot : SLOTS) {
            List<Integer> counts = new ArrayList<>();
            for (LocalDate date : weekDates) {
                String key = date + "|" + slot;
                counts.add(weeklyCountMap.getOrDefault(key, 0));
            }
            weeklyRows.add(new AdminWeeklySlotRowDto(slot, counts));
        }
        AdminWeeklyReservationTableDto weeklyTable = new AdminWeeklyReservationTableDto(
                weekDates.stream().map(LocalDate::toString).toList(),
                weeklyRows
        );

        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDateTime createdFrom = monthStart.atStartOfDay();
        LocalDateTime createdTo = today.plusDays(1).atStartOfDay().minusNanos(1);
        List<Reservation> monthlyReceived = reservationRepository.findByCreatedAtBetween(createdFrom, createdTo);
        Map<LocalDate, Integer> dailyAcceptedMap = new HashMap<>();
        for (Reservation reservation : monthlyReceived) {
            if (reservation.getCreatedAt() == null) {
                continue;
            }
            LocalDate date = reservation.getCreatedAt().toLocalDate();
            dailyAcceptedMap.put(date, dailyAcceptedMap.getOrDefault(date, 0) + 1);
        }

        List<AdminDailyCountDto> monthlyTrend = new ArrayList<>();
        for (LocalDate day = monthStart; !day.isAfter(today); day = day.plusDays(1)) {
            monthlyTrend.add(new AdminDailyCountDto(day.toString(), dailyAcceptedMap.getOrDefault(day, 0)));
        }

        return new AdminReservationSummaryDto(today.toString(), weeklyTable, monthlyTrend);
    }

    private SlotAvailabilityDto computeSlotAvailability(String course, LocalDate reserveDate, String reserveTime) {
        int capacity = slotCapacityRepository
                .findByReserveDateAndReserveTime(reserveDate, reserveTime)
                .map(SlotCapacity::getCapacity)
                .orElse(DEFAULT_CAPACITY);
        int used = reservationRepository.sumParticipants(course, reserveDate, reserveTime);
        int remaining = Math.max(capacity - used, 0);
        boolean bookable = remaining > 0;
        String status = bookable ? "OPEN" : "OUT_OF_SCOPE";
        return new SlotAvailabilityDto(reserveTime, remaining, status, bookable);
    }

    private String normalizeCourse(String course) {
        if (course == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "course is required.");
        }
        String normalized = course.trim().toLowerCase(Locale.ROOT);
        if (!COURSES.contains(normalized)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown course.");
        }
        return normalized;
    }
}
