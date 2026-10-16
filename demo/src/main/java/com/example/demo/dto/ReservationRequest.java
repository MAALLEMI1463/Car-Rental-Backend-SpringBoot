package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Used to create a reservation, for both the ADMIN flow (POST
 * /api/admin/reservations/users/{userId}) and the CLIENT flow (POST
 * /api/client/reservations). There is intentionally no userId field here:
 * the admin flow takes the target user from the URL path, and the client
 * flow always takes it from the JWT — never from the request body.
 *
 * Only the pickup date/time and the return DATE are collected; the return
 * time is always derived server-side from the pickup time (see
 * ReservationService), so the frontend cannot manipulate the rental length.
 */
public class ReservationRequest {

    @NotNull(message = "Car is required")
    private Long carId;

    @NotNull(message = "Pickup date/time is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime pickupDateTime;

    @NotNull(message = "Return date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate returnDate;

    public ReservationRequest() {
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public LocalDateTime getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(LocalDateTime pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}