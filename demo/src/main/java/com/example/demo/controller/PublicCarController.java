package com.example.demo.controller;

import com.example.demo.dto.CarResponse;
import com.example.demo.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Public, read-only car browsing for anonymous visitors and CLIENT users
 * (see Task 3 - Client Side Home). Explicitly permitAll'd for GET in
 * SecurityConfig, so no JWT is required here. Always delegates to
 * CarService, which already restricts results to is_deleted = false and
 * sanitizes the search keyword — the exact same guarantees as the
 * ADMIN-only /api/admin/cars endpoints.
 */
@RestController
@RequestMapping("/api/cars")
public class PublicCarController {

    private final CarService carService;

    public PublicCarController(CarService carService) {
        this.carService = carService;
    }

    /** Lists active cars; optional ?keyword= searches by brand or model. */
    @GetMapping
    public ResponseEntity<List<CarResponse>> listCars(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(carService.getAllActiveCars(keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCar(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }
}