package com.example.humorie.reservation.dto;


import com.example.humorie.consultant.counselor.entity.Counselor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class ReservationDto {

    private final Long reservationId;

    private final Counselor counselor;

    private final LocalDateTime counselDate;

    private final LocalDateTime createdAt;


    @Builder
    public ReservationDto(Long reservationId, Counselor counselor, LocalDateTime counselDate, LocalDateTime createdAt) {
        this.reservationId = reservationId;
        this.counselor = counselor;
        this.counselDate = counselDate;
        this.createdAt = createdAt;
    }
}