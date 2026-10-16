package com.example.demo.service;

import com.example.demo.dto.AdminStatisticsResponse;
import com.example.demo.dto.MonthlyStatResponse;
import com.example.demo.dto.StatisticsSummaryResponse;
import com.example.demo.dto.StatusCountResponse;
import com.example.demo.dto.TopCarResponse;
import com.example.demo.model.Car;
import com.example.demo.model.CarStatus;
import com.example.demo.model.Reservation;
import com.example.demo.model.ReservationStatus;
import com.example.demo.model.Role;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private static final String[] MONTH_LABELS =
            {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final ReservationRepository reservationRepository;

    public StatisticsService(UserRepository userRepository,
                              CarRepository carRepository,
                              ReservationRepository reservationRepository) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.reservationRepository = reservationRepository;
    }

    /**
     * readOnly=true keeps the persistence context open for this whole
     * method, so the lazily-loaded Reservation.car association used below
     * (for the "most reserved cars" breakdown) can be accessed safely
     * regardless of the app's open-in-view setting.
     */
    @Transactional(readOnly = true)
    public AdminStatisticsResponse getStatistics() {
        List<Reservation> allReservations = reservationRepository.findAll();

        StatisticsSummaryResponse summary = buildSummary(allReservations);
        List<MonthlyStatResponse> monthlyStats = buildMonthlyStats(allReservations);
        List<StatusCountResponse> byStatus = buildStatusCounts(allReservations);
        List<TopCarResponse> topCars = buildTopCars(allReservations);

        return new AdminStatisticsResponse(summary, monthlyStats, byStatus, topCars);
    }

    private StatisticsSummaryResponse buildSummary(List<Reservation> reservations) {
        long totalUsers = userRepository.countByRole(Role.CLIENT);
        long totalCars = carRepository.countByIsDeletedFalse();
        long totalAvailable = carRepository.countByIsDeletedFalseAndCarStatus(CarStatus.AVAILABLE);
        long totalUnavailable = carRepository.countByIsDeletedFalseAndCarStatus(CarStatus.UNAVAILABLE);

        // Total Revenue only counts reservations that actually generated
        // (or are generating) money: ACTIVE (car currently out, being paid
        // for) or COMPLETED (rental finished). PENDING/CONFIRMED haven't
        // earned anything yet, so they're excluded.
        BigDecimal totalRevenue = reservations.stream()
                .filter(r -> r.getReservationStatus() == ReservationStatus.ACTIVE
                        || r.getReservationStatus() == ReservationStatus.COMPLETED)
                .map(Reservation::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new StatisticsSummaryResponse(
                totalUsers, totalCars, totalAvailable, totalUnavailable, reservations.size(), totalRevenue);
    }

    /** Current calendar year, Jan through Dec, grouped by when the reservation was created. */
    private List<MonthlyStatResponse> buildMonthlyStats(List<Reservation> reservations) {
        int currentYear = LocalDate.now().getYear();

        long[] counts = new long[12];
        BigDecimal[] revenues = new BigDecimal[12];
        for (int i = 0; i < 12; i++) {
            revenues[i] = BigDecimal.ZERO;
        }

        for (Reservation r : reservations) {
            LocalDateTime createdAt = r.getCreatedAt();
            if (createdAt == null || createdAt.getYear() != currentYear) {
                continue;
            }
            int idx = createdAt.getMonthValue() - 1;
            counts[idx]++;

            // Same rule as the Total Revenue card: only ACTIVE/COMPLETED
            // reservations count as revenue.
            if (r.getReservationStatus() == ReservationStatus.ACTIVE
                    || r.getReservationStatus() == ReservationStatus.COMPLETED) {
                BigDecimal price = r.getTotalPrice() != null ? r.getTotalPrice() : BigDecimal.ZERO;
                revenues[idx] = revenues[idx].add(price);
            }
        }

        List<MonthlyStatResponse> result = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            result.add(new MonthlyStatResponse(MONTH_LABELS[i], counts[i], revenues[i]));
        }
        return result;
    }

    /** Always returns all 4 statuses, even ones with a count of 0, so the pie chart is stable. */
    private List<StatusCountResponse> buildStatusCounts(List<Reservation> reservations) {
        Map<ReservationStatus, Long> counts = reservations.stream()
                .collect(Collectors.groupingBy(Reservation::getReservationStatus, Collectors.counting()));

        List<StatusCountResponse> result = new ArrayList<>();
        for (ReservationStatus status : ReservationStatus.values()) {
            result.add(new StatusCountResponse(status.name(), counts.getOrDefault(status, 0L)));
        }
        return result;
    }

    private List<TopCarResponse> buildTopCars(List<Reservation> reservations) {
        Map<Car, Long> counts = reservations.stream()
                .collect(Collectors.groupingBy(Reservation::getCar, Collectors.counting()));

        return counts.entrySet().stream()
                .sorted(Map.Entry.<Car, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> new TopCarResponse(e.getKey().getId(), e.getKey().getBrand(), e.getKey().getModel(), e.getValue()))
                .toList();
    }
}