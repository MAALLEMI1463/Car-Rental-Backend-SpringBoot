package com.example.demo.controller;

import com.example.demo.dto.AdminStatisticsResponse;
import com.example.demo.service.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin Panel statistics (Task 5). Protected twice, same pattern as the
 * other admin controllers: SecurityConfig maps "/api/admin/**" to
 * hasRole("ADMIN") (every request also passes JwtAuthenticationFilter
 * first, which rejects missing/invalid/expired tokens), plus @PreAuthorize
 * here as a defensive second layer.
 */
@RestController
@RequestMapping("/api/admin/statistics")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatisticsController {

    private final StatisticsService statisticsService;

    public AdminStatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping
    public ResponseEntity<AdminStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(statisticsService.getStatistics());
    }
}