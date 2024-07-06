package com.example.humorie.reservation.dto.response;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class AvailableReservationTimesResDto {
    List<LocalTime> availableTimes;

    public AvailableReservationTimesResDto(List<LocalTime> availableTimes){
        this.availableTimes = availableTimes;
    }
}
