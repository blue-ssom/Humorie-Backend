package com.example.humorie.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class CreateReservationReq {

    private Long counselorId;

    private String location;

    private LocalDate counselDate;

    private LocalTime counselTime;
}
