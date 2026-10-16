package com.example.demo.controller;

import com.example.demo.dto.CarResponse;
import com.example.demo.dto.ReservationRequest;
import com.example.demo.dto.ReservationResponse;
import com.example.demo.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * CLIENT reservation workflow (Task 4):
 *   pick dates -> list available cars -> book. The booking user is always
 *   taken from the authenticated JWT (Authentication#getName()) — the
 *   request body has no userId field at all, so the frontend cannot send one.
 * Protected by "/api/client/**" -> hasRole("CLIENT") in SecurityConfig, plus
 * @PreAuthorize here as a defensive second layer.
 */
@RestController
@RequestMapping("/api/client/reservations")
@PreAuthorize("hasRole('CLIENT')")
public class ClientReservationController {

    private final ReservationService reservationService;

    public ClientReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/available-cars")
    public ResponseEntity<List<CarResponse>> getAvailableCars(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime pickupDateTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate) {
        return ResponseEntity.ok(reservationService.getAvailableCars(pickupDateTime, returnDate));
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(@Valid @RequestBody ReservationRequest request,
                                                        Authentication authentication) {
        ReservationResponse response =
                reservationService.createReservationForClient(authentication.getName(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ReservationResponse>> myReservations(Authentication authentication) {
        return ResponseEntity.ok(reservationService.getMyReservations(authentication.getName()));
    }

    /** Only the owner can delete, and only while PENDING or CONFIRMED. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        reservationService.deleteByClient(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}