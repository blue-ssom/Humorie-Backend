package com.example.humorie.reservation.repository;

import com.example.humorie.reservation.entity.Reservation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByAccount_EmailOrderByCreatedAtDesc(String accountEmail);

    int countByCounselorIdAndCounselDate(Long counselorId, LocalDate counselDate);

    boolean existsByCounselorIdAndCounselDateAndCounselTime(Long counselorId,LocalDate counselDate, LocalTime counselTime);

    Optional<Reservation> findReservationByReservationUid(String uid);
}
