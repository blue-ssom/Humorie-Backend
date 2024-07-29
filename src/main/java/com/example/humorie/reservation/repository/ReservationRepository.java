package com.example.humorie.reservation.repository;

import com.example.humorie.reservation.entity.Reservation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.account.email = :accountEmail ORDER BY r.createdAt DESC")
    List<Reservation> findAllByAccountEmailOrderByCreatedAtDesc(@Param("accountEmail") String accountEmail);

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.counselor.id = :counselorId AND r.counselDate = :counselDate")
    int countByCounselorIdAndCounselDate(@Param("counselorId") Long counselorId, @Param("counselDate") LocalDate counselDate);

    @Query("SELECT COUNT(r) > 0 FROM Reservation r WHERE r.counselor.id = :counselorId AND r.counselDate = :counselDate AND r.counselTime = :counselTime")
    boolean existsByCounselorIdAndCounselDateAndCounselTime(@Param("counselorId") Long counselorId, @Param("counselDate") LocalDate counselDate, @Param("counselTime") LocalTime counselTime);

    Optional<Reservation> findReservationByReservationUid(String uid);

    @Transactional
    void deleteByAccount_Id(Long accountId);

}
