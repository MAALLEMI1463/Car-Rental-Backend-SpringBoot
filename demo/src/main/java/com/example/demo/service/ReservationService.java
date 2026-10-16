package com.example.demo.service;

import com.example.demo.dto.CarResponse;
import com.example.demo.dto.ReservationRequest;
import com.example.demo.dto.ReservationResponse;
import com.example.demo.exception.CarNotFoundException;
import com.example.demo.exception.ReservationNotFoundException;
import com.example.demo.model.Car;
import com.example.demo.model.Reservation;
import com.example.demo.model.ReservationStatus;
import com.example.demo.model.User;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    /** Strict forward order — no skipping, no going backward. */
    private static final List<ReservationStatus> STATUS_ORDER =
            List.of(ReservationStatus.PENDING, ReservationStatus.CONFIRMED,
                    ReservationStatus.ACTIVE, ReservationStatus.COMPLETED);

    private final ReservationRepository reservationRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository,
                               CarRepository carRepository,
                               UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    /**
     * rental_end = returnDate + pickup's time-of-day (the user never picks a
     * return time). Computed here only, so the frontend can never manipulate
     * the rental length.
     */
    private LocalDateTime computeRentalEnd(LocalDateTime pickupDateTime, LocalDate returnDate) {
        return returnDate.atTime(pickupDateTime.toLocalTime());
    }

    private void validatePeriod(LocalDateTime start, LocalDateTime end) {
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("Return date must be after the pick-up date");
        }
    }

    public List<CarResponse> getAvailableCars(LocalDateTime pickupDateTime, LocalDate returnDate) {
        LocalDateTime rentalEnd = computeRentalEnd(pickupDateTime, returnDate);
        validatePeriod(pickupDateTime, rentalEnd);

        return carRepository.findAvailableCars(pickupDateTime, rentalEnd)
                .stream()
                .map(CarResponse::new)
                .toList();
    }

    private Reservation buildReservation(User user, ReservationRequest request) {
        Car car = carRepository.findByIdAndIsDeletedFalse(request.getCarId())
                .orElseThrow(() -> new CarNotFoundException(request.getCarId()));

        LocalDateTime start = request.getPickupDateTime();
        LocalDateTime end = computeRentalEnd(start, request.getReturnDate());
        validatePeriod(start, end);

        if (car.getCarStatus() == com.example.demo.model.CarStatus.UNAVAILABLE) {
            throw new IllegalStateException("This car is not available for booking");
        }

        // Re-checked here (not just when the "available cars" list was
        // fetched) to close the race window between browsing and booking.
        if (reservationRepository.existsOverlapping(car.getId(), start, end)) {
            throw new IllegalStateException("This car is no longer available for the selected period");
        }

        long days = ChronoUnit.DAYS.between(start, end);
        BigDecimal totalPrice = car.getPricePerDay().multiply(BigDecimal.valueOf(days));

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setCar(car);
        reservation.setStartDate(start);
        reservation.setEndDate(end);
        reservation.setPricePerDay(car.getPricePerDay());
        reservation.setTotalPrice(totalPrice);
        reservation.setReservationStatus(ReservationStatus.PENDING);
        return reservation;
    }

    /** CLIENT flow: user comes from the authenticated username (JWT), never from the request body. */
    public ReservationResponse createReservationForClient(String username, ReservationRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));
        Reservation reservation = buildReservation(user, request);
        return new ReservationResponse(reservationRepository.save(reservation));
    }

    /** ADMIN flow: books on behalf of the given client user id. */
    public ReservationResponse createReservationForAdmin(Long userId, ReservationRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        Reservation reservation = buildReservation(user, request);
        return new ReservationResponse(reservationRepository.save(reservation));
    }

    public List<ReservationResponse> getMyReservations(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));
        return reservationRepository.findByUser_IdOrderByCreatedAtDesc(user.getId())
                .stream().map(ReservationResponse::new).toList();
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(ReservationResponse::new).toList();
    }

    /** ADMIN only. Enforces strictly sequential transitions - no skipping, no going back. */
    public ReservationResponse updateStatus(Long id, ReservationStatus newStatus) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        int currentIndex = STATUS_ORDER.indexOf(reservation.getReservationStatus());
        int newIndex = STATUS_ORDER.indexOf(newStatus);

        if (newIndex != currentIndex + 1) {
            throw new IllegalArgumentException(
                    "Invalid status transition: " + reservation.getReservationStatus() + " -> " + newStatus);
        }

        reservation.setReservationStatus(newStatus);
        return new ReservationResponse(reservationRepository.save(reservation));
    }

    /** ADMIN only: may delete a reservation only while it's still CONFIRMED. */
    public void deleteByAdmin(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
        if (reservation.getReservationStatus() != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Only CONFIRMED reservations can be deleted");
        }
        reservationRepository.delete(reservation);
    }

    /** CLIENT only: may delete their OWN reservation while it's PENDING or CONFIRMED. */
    public void deleteByClient(String username, Long id) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));

        Reservation reservation = reservationRepository.findByIdAndUser_Id(id, user.getId())
                .orElseThrow(() -> new ReservationNotFoundException(id));

        ReservationStatus status = reservation.getReservationStatus();
        if (status != ReservationStatus.PENDING && status != ReservationStatus.CONFIRMED) {
            throw new IllegalStateException("Only PENDING or CONFIRMED reservations can be deleted");
        }
        reservationRepository.delete(reservation);
    }
}