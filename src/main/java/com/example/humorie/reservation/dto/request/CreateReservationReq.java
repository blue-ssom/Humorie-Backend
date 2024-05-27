package com.example.humorie.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CreateReservationReq {

    private Long counselorId;

    private LocalDateTime counselDate;
}
