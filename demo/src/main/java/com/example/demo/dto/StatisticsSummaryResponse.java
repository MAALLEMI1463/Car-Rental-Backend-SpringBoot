package com.example.demo.dto;

import java.math.BigDecimal;

/** The 6 top statistics cards on the Admin Panel. */
public class StatisticsSummaryResponse {

private long totalUsers; // users with role = CLIENT
    private long totalCars;
    private long totalAvailableCars;
    private long totalUnavailableCars;
    private long totalReservations;
    private BigDecimal totalRevenue;

    public StatisticsSummaryResponse() {
    }

    public StatisticsSummaryResponse(long totalUsers, long totalCars, long totalAvailableCars,
                                      long totalUnavailableCars, long totalReservations, BigDecimal totalRevenue) {
        this.totalUsers = totalUsers;
        this.totalCars = totalCars;
        this.totalAvailableCars = totalAvailableCars;
        this.totalUnavailableCars = totalUnavailableCars;
        this.totalReservations = totalReservations;
        this.totalRevenue = totalRevenue;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalCars() {
        return totalCars;
    }

    public void setTotalCars(long totalCars) {
        this.totalCars = totalCars;
    }

    public long getTotalAvailableCars() {
        return totalAvailableCars;
    }

    public void setTotalAvailableCars(long totalAvailableCars) {
        this.totalAvailableCars = totalAvailableCars;
    }

    public long getTotalUnavailableCars() {
        return totalUnavailableCars;
    }

    public void setTotalUnavailableCars(long totalUnavailableCars) {
        this.totalUnavailableCars = totalUnavailableCars;
    }

    public long getTotalReservations() {
        return totalReservations;
    }

    public void setTotalReservations(long totalReservations) {
        this.totalReservations = totalReservations;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}