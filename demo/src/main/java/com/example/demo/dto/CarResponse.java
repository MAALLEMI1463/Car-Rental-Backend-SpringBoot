package com.example.demo.dto;

import com.example.demo.model.Car;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CarResponse {

    private Long id;
    private String brand;
    private String model;
    private String registrationNumber;
    private String imageUrl;
    private BigDecimal pricePerDay;
    private String description;
    private String carStatus;
    private LocalDateTime createdAt;

    public CarResponse() {
    }

    public CarResponse(Car car) {
        this.id = car.getId();
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.registrationNumber = car.getRegistrationNumber();
        this.imageUrl = car.getImageUrl();
        this.pricePerDay = car.getPricePerDay();
        this.description = car.getDescription();
        this.carStatus = car.getCarStatus().name();
        this.createdAt = car.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(String carStatus) {
        this.carStatus = carStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}