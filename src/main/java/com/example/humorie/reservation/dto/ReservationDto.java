package com.example.humorie.reservation.dto;


import com.example.humorie.consultant.counselor.entity.Counselor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class ReservationDto {

    private final Long reservationId;

    private final String counselorName;

    private final boolean isOnline;

    private final String location;

    private final LocalDate counselDate;

    private final LocalTime counselTime;

    private final LocalDateTime createdAt;


    @Builder
    public ReservationDto(Long reservationId, String counselorName, boolean isOnline, String location,
                          LocalDate counselDate, LocalTime counselTime, LocalDateTime createdAt) {
        this.reservationId = reservationId;
        this.counselorName = counselorName;
        this.isOnline = isOnline;
        this.location = location;
        this.counselDate = counselDate;
        this.counselTime = counselTime;
        this.createdAt = createdAt;
    }
}