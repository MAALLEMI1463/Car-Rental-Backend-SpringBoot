package com.example.demo.service;

import com.example.demo.dto.CarRequest;
import com.example.demo.dto.CarResponse;
import com.example.demo.exception.CarNotFoundException;
import com.example.demo.model.Car;
import com.example.demo.model.CarStatus;
import com.example.demo.repository.CarRepository;
import com.example.demo.util.InputSanitizer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * The image itself was already uploaded to Cloudinary by the frontend;
     * this only persists the URLs it returned. An image is required to
     * create a car.
     */
    public CarResponse createCar(CarRequest request) {
        if (request.getImageUrl() == null || request.getImageUrl().isBlank()) {
            throw new IllegalArgumentException("Car image is required");
        }

        Car car = new Car();
        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setRegistrationNumber(request.getRegistrationNumber());
        car.setPricePerDay(request.getPricePerDay());
        car.setDescription(request.getDescription());
        car.setCarStatus(request.getCarStatus() != null ? request.getCarStatus() : CarStatus.AVAILABLE);
        car.setImageUrl(request.getImageUrl());
        car.setImagePublicId(request.getImagePublicId());

        return new CarResponse(carRepository.save(car));
    }

    public CarResponse updateCar(Long id, CarRequest request) {
        Car car = carRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setRegistrationNumber(request.getRegistrationNumber());
        car.setPricePerDay(request.getPricePerDay());
        car.setDescription(request.getDescription());
        if (request.getCarStatus() != null) {
            car.setCarStatus(request.getCarStatus());
        }
        // Only replace the image if a new one was uploaded; otherwise keep
        // the car's existing Cloudinary image.
        if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            car.setImageUrl(request.getImageUrl());
            car.setImagePublicId(request.getImagePublicId());
        }

        return new CarResponse(carRepository.save(car));
    }

    /** Soft delete: sets is_deleted = 1, never removes the row. */
    public void softDeleteCar(Long id) {
        Car car = carRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        car.setIsDeleted(true);
        carRepository.save(car);
    }

    public CarResponse getCarById(Long id) {
        Car car = carRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        return new CarResponse(car);
    }

    /** Lists only non-deleted cars; if a keyword is given, filters by brand or model. */
    public List<CarResponse> getAllActiveCars(String keyword) {
        String cleanKeyword = InputSanitizer.sanitizeSearchKeyword(keyword);

        List<Car> cars = (cleanKeyword == null)
                ? carRepository.findByIsDeletedFalseOrderByCreatedAtDesc()
                : carRepository.searchActiveByBrandOrModel(cleanKeyword);

        return cars.stream().map(CarResponse::new).toList();
    }
}