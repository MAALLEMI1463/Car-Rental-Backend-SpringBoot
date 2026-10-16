package com.example.demo.dto;

import java.util.List;

/**
 * Everything the Admin Panel page needs, in one response — cards, both
 * monthly line charts' data, the status pie chart, and the top-5 bar chart.
 * Bundled into a single endpoint so the frontend can drive the whole page
 * off one React Query hook with one refetchInterval.
 */
public class AdminStatisticsResponse {

    private StatisticsSummaryResponse summary;
    private List<MonthlyStatResponse> monthlyStats;
    private List<StatusCountResponse> reservationsByStatus;
    private List<TopCarResponse> mostReservedCars;

    public AdminStatisticsResponse() {
    }

    public AdminStatisticsResponse(StatisticsSummaryResponse summary,
                                    List<MonthlyStatResponse> monthlyStats,
                                    List<StatusCountResponse> reservationsByStatus,
                                    List<TopCarResponse> mostReservedCars) {
        this.summary = summary;
        this.monthlyStats = monthlyStats;
        this.reservationsByStatus = reservationsByStatus;
        this.mostReservedCars = mostReservedCars;
    }

    public StatisticsSummaryResponse getSummary() {
        return summary;
    }

    public void setSummary(StatisticsSummaryResponse summary) {
        this.summary = summary;
    }

    public List<MonthlyStatResponse> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(List<MonthlyStatResponse> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }

    public List<StatusCountResponse> getReservationsByStatus() {
        return reservationsByStatus;
    }

    public void setReservationsByStatus(List<StatusCountResponse> reservationsByStatus) {
        this.reservationsByStatus = reservationsByStatus;
    }

    public List<TopCarResponse> getMostReservedCars() {
        return mostReservedCars;
    }

    public void setMostReservedCars(List<TopCarResponse> mostReservedCars) {
        this.mostReservedCars = mostReservedCars;
    }
}