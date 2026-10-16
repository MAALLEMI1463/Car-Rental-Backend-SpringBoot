package com.example.demo.dto;

import com.example.demo.model.Reservation;
import com.example.demo.model.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservationResponse {

    private Long id;
    private Long userId;
    private String username;
    private Long carId;
    private String carBrand;
    private String carModel;
    private String carImageUrl;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal pricePerDay;
    private BigDecimal totalPrice;
    private String reservationStatus;
    private boolean pastDue;
    private LocalDateTime createdAt;

    public ReservationResponse() {
    }

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.userId = reservation.getUser().getId();
        this.username = reservation.getUser().getUsername();
        this.carId = reservation.getCar().getId();
        this.carBrand = reservation.getCar().getBrand();
        this.carModel = reservation.getCar().getModel();
        this.carImageUrl = reservation.getCar().getImageUrl();
        this.startDate = reservation.getStartDate();
        this.endDate = reservation.getEndDate();
        this.pricePerDay = reservation.getPricePerDay();
        this.totalPrice = reservation.getTotalPrice();
        this.reservationStatus = reservation.getReservationStatus().name();
        // Per spec: flag reservations whose rental period has already
        // started but that haven't reached COMPLETED yet.
        this.pastDue = reservation.getStartDate().isBefore(LocalDateTime.now())
                && reservation.getReservationStatus() != ReservationStatus.COMPLETED;
        this.createdAt = reservation.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarImageUrl() {
        return carImageUrl;
    }

    public void setCarImageUrl(String carImageUrl) {
        this.carImageUrl = carImageUrl;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public boolean isPastDue() {
        return pastDue;
    }

    public void setPastDue(boolean pastDue) {
        this.pastDue = pastDue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
