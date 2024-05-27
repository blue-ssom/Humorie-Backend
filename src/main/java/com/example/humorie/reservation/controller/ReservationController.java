package com.example.humorie.reservation.controller;

import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.reservation.dto.request.CreateReservationReq;
import com.example.humorie.reservation.entity.Reservation;
import com.example.humorie.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Reservation" , description = "Reservation 관련 API 모음")
@RequiredArgsConstructor
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "상담 예약 생성")
    @PostMapping("/create")
    public ResponseEntity<String> createReservation(@AuthenticationPrincipal PrincipalDetails principal,
                                                    @RequestBody @Valid CreateReservationReq createReservationReq){
        return reservationService.createReservation(principal, createReservationReq);
    }

//    public ResponseEntity<List<Reservation>> getReservations(@AuthenticationPrincipal UserDetails user){
//
//    }


}
