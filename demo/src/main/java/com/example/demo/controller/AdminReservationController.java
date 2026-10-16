package com.example.demo.controller;

import com.example.demo.dto.CarResponse;
import com.example.demo.dto.ReservationRequest;
import com.example.demo.dto.ReservationResponse;
import com.example.demo.dto.ReservationStatusUpdateRequest;
import com.example.demo.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * ADMIN reservation workflow (Task 4):
 *   search a client -> pick dates -> list available cars -> book on their behalf.
 * Protected twice: SecurityConfig maps "/api/admin/**" to hasRole("ADMIN"),
 * plus @PreAuthorize here as a defensive second layer, same pattern as the
 * other admin controllers.
 */
@RestController
@RequestMapping("/api/admin/reservations")
@PreAuthorize("hasRole('ADMIN')")
public class AdminReservationController {

    private final ReservationService reservationService;

    public AdminReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /** Cars available for the requested period (is_deleted=false, not UNAVAILABLE, no overlapping reservation). */
    @GetMapping("/available-cars")
    public ResponseEntity<List<CarResponse>> getAvailableCars(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime pickupDateTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate) {
        return ResponseEntity.ok(reservationService.getAvailableCars(pickupDateTime, returnDate));
    }

    /** Creates a reservation on behalf of the given client (userId comes from the URL, never the body). */
    @PostMapping("/users/{userId}")
    public ResponseEntity<ReservationResponse> createForUser(@PathVariable Long userId,
                                                               @Valid @RequestBody ReservationRequest request) {
        ReservationResponse response = reservationService.createReservationForAdmin(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> listAll() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    /** Strictly sequential: PENDING -> CONFIRMED -> ACTIVE -> COMPLETED, no skipping. */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ReservationResponse> updateStatus(@PathVariable Long id,
                                                              @Valid @RequestBody ReservationStatusUpdateRequest request) {
        return ResponseEntity.ok(reservationService.updateStatus(id, request.getStatus()));
    }

    /** Only reservations still in CONFIRMED can be deleted. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteByAdmin(id);
        return ResponseEntity.noContent().build();
    }
}