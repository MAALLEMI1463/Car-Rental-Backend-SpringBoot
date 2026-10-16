package com.example.demo.dto;

import java.math.BigDecimal;

/** One data point for the two monthly line charts (current calendar year, Jan-Dec). */
public class MonthlyStatResponse {

    private String month; // "Jan", "Feb", ...
    private long reservationCount;
    private BigDecimal revenue;

    public MonthlyStatResponse() {
    }

    public MonthlyStatResponse(String month, long reservationCount, BigDecimal revenue) {
        this.month = month;
        this.reservationCount = reservationCount;
        this.revenue = revenue;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(long reservationCount) {
        this.reservationCount = reservationCount;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }
}