package com.example.humorie.reservation.dto.response;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class AvailableReservationDatesResDto {
    List<LocalDate> availableDates;

    public AvailableReservationDatesResDto(List<LocalDate> availableDates){
        this.availableDates = availableDates;
    }
}
