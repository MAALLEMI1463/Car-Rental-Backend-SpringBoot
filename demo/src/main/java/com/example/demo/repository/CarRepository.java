package com.example.demo.repository;

import com.example.demo.model.Car;
import com.example.demo.model.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByIsDeletedFalseOrderByCreatedAtDesc();

    Optional<Car> findByIdAndIsDeletedFalse(Long id);

    boolean existsByRegistrationNumber(String registrationNumber);

    /** Used by the Admin Panel statistics cards (Task 5). */
    long countByIsDeletedFalse();

    long countByIsDeletedFalseAndCarStatus(CarStatus carStatus);

    /**
     * Search by brand OR model, always restricted to non-deleted cars
     * (is_deleted = 0), per the "search a car by brand or model" requirement.
     */
    @Query("SELECT c FROM Car c WHERE c.isDeleted = false AND "
            + "(LOWER(c.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(c.model) LIKE LOWER(CONCAT('%', :keyword, '%'))) "
            + "ORDER BY c.createdAt DESC")
    List<Car> searchActiveByBrandOrModel(@Param("keyword") String keyword);

    /**
     * Cars available for the given rental period (Task 4):
     * is_deleted = false AND car_status <> UNAVAILABLE AND no overlapping
     * reservation with status PENDING/CONFIRMED/ACTIVE for that period.
     * Overlap test: existingStart < requestedEnd AND existingEnd > requestedStart.
     */
    @Query("SELECT c FROM Car c WHERE c.isDeleted = false AND c.carStatus <> 'UNAVAILABLE' "
            + "AND c.id NOT IN ("
            + "  SELECT r.car.id FROM Reservation r WHERE r.reservationStatus IN ('PENDING','CONFIRMED','ACTIVE') "
            + "  AND r.startDate < :requestedEnd AND r.endDate > :requestedStart"
            + ") ORDER BY c.createdAt DESC")
    List<Car> findAvailableCars(@Param("requestedStart") LocalDateTime requestedStart,
                                 @Param("requestedEnd") LocalDateTime requestedEnd);
}