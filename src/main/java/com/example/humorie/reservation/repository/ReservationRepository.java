package com.example.humorie.reservation.repository;

import com.example.humorie.reservation.entity.Reservation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.account.email = :accountEmail ORDER BY r.createdAt DESC")
    List<Reservation> findAllByAccountEmailOrderByCreatedAtDesc(@Param("accountEmail") String accountEmail);


}
