package com.example.humorie.reservation.service;

import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.counselor.entity.Counselor;
import com.example.humorie.counselor.repository.CounselorRepository;
import com.example.humorie.reservation.dto.request.CreateReservationReq;
import com.example.humorie.reservation.entity.Reservation;
import com.example.humorie.reservation.repository.ReservationRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CounselorRepository counselorRepository;

    public ResponseEntity<String> createReservation(PrincipalDetails principal, CreateReservationReq createReservationReq) {
        Counselor counselor = counselorRepository.findById(createReservationReq.getCounselorId())
                .orElseThrow(() -> new RuntimeException("Counselor Not Found"));


        Reservation reservation = Reservation.builder()
                .account(principal.getAccountDetail())
                .counselor(counselor)
                .counselDate(createReservationReq.getCounselDate())
                .build();


        reservationRepository.save(reservation);
        return ResponseEntity.ok("Success creating reservation");
    }

}
