package com.example.demo.controller;

import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All endpoints here are ADMIN-only, enforced twice:
 *  - SecurityConfig maps "/api/admin/**" to hasRole("ADMIN")
 *  - @PreAuthorize below, as a defensive second layer
 *
 * Every request also passes through JwtAuthenticationFilter first, which
 * rejects missing, invalid, or expired tokens before this controller is
 * ever reached.
 */
@RestController
@RequestMapping("/api/admin/cars")
@PreAuthorize("hasRole('ADMIN')")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<CarResponse> createCar(@Valid @RequestBody CarRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.createCar(request));
    }

    /** Lists active (is_deleted=0) cars; optional ?keyword= searches by brand or model. */
    @GetMapping
    public ResponseEntity<List<CarResponse>> listCars(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(carService.getAllActiveCars(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCar(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(@PathVariable Long id, @Valid @RequestBody CarRequest request) {
        return ResponseEntity.ok(carService.updateCar(id, request));
    }

    /** Soft delete only: sets is_deleted = 1, never removes the row. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.softDeleteCar(id);
        return ResponseEntity.noContent().build();
    }
}