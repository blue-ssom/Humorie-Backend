package com.example.humorie.reservation.service;

import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.counselor.entity.Counselor;
import com.example.humorie.counselor.repository.CounselorRepository;
import com.example.humorie.reservation.dto.ReservationDto;
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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

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

    public ResponseEntity<List<ReservationDto>> getReservations(PrincipalDetails principal) {
        List<Reservation> reservations = reservationRepository.findAllByAccountEmailOrderByCreatedAtDesc(principal.getUsername());
        List<ReservationDto> reservationDtos = new ArrayList<>();

        for(Reservation reservation : reservations) {
            ReservationDto reservationDto = new ReservationDto(reservation.getId(),
                    reservation.getCounselor(),
                    reservation.getCounselDate(),
                    reservation.getCreatedAt());

            reservationDtos.add(reservationDto);
        }
        return ResponseEntity.ok(reservationDtos);
    }

}
