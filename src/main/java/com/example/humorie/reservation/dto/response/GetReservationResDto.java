package com.example.humorie.reservation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetReservationResDto {
    String ReservationUid;

    String counselorName;

    String buyerName;

    String buyerEmail;

    Integer finalPrice;
}
