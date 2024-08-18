package com.example.humorie.reservation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateReservationResDto {
    String reservationUid;
    String counselorName;
    Integer finalPrice;
    String accountName;
    String accountEmail;
    // 가격, 예약자 이름, 예약자 이메일, 상담사 이름

}
