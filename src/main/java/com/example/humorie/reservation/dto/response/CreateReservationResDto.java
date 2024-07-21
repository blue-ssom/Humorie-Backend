package com.example.humorie.reservation.dto.response;

import lombok.Getter;

@Getter
public class CreateReservationResDto {
    String reservationUid;

    public CreateReservationResDto(String reservationUid){
        this.reservationUid = reservationUid;
    }
}
