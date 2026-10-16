package com.example.demo.dto;

/** One bar of the "most reserved cars" Bar chart (top 5). */
public class TopCarResponse {
    
    private Long carId;
    private String brand;
    private String model;
    private long reservationCount;

    public TopCarResponse() {
    }

    public TopCarResponse(Long carId, String brand, String model, long reservationCount) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.reservationCount = reservationCount;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
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

    public long getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(long reservationCount) {
        this.reservationCount = reservationCount;
    }
}