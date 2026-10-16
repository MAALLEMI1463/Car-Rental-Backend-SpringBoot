package com.example.demo.repository;

import com.example.demo.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser_IdOrderByCreatedAtDesc(Long userId);

    List<Reservation> findAllByOrderByCreatedAtDesc();

    Optional<Reservation> findByIdAndUser_Id(Long id, Long userId);

    /**
     * Re-checked at the moment of booking (not just when listing "available"
     * cars) to guard against a race where two people book the same car for
     * an overlapping period between the availability check and the save.
     */
    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.car.id = :carId "
            + "AND r.reservationStatus IN ('PENDING','CONFIRMED','ACTIVE') "
            + "AND r.startDate < :end AND r.endDate > :start")
    boolean existsOverlapping(@Param("carId") Long carId,
                              @Param("start") LocalDateTime start,
                              @Param("end") LocalDateTime end);
}
