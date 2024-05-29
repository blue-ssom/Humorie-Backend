package com.example.humorie.reservation.service;

import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.counselor.entity.Counselor;
import com.example.humorie.counselor.repository.CounselorRepository;
import com.example.humorie.reservation.dto.ReservationDto;
import com.example.humorie.reservation.dto.request.CreateReservationReq;
import com.example.humorie.reservation.entity.Reservation;
import com.example.humorie.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CounselorRepository counselorRepository;

    public ResponseEntity<String> createReservation(PrincipalDetails principal, CreateReservationReq createReservationReq) {
        Counselor counselor = counselorRepository.findById(createReservationReq.getCounselorId())
                .orElseThrow(() -> new RuntimeException("Counselor Not Found"));

        if(principal == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Reservation reservation = Reservation.builder()
                .account(principal.getAccountDetail())
                .counselor(counselor)
                .counselDate(createReservationReq.getCounselDate())
                .build();


        reservationRepository.save(reservation);
        return ResponseEntity.ok("Success creating reservation");
    }

    public ResponseEntity<List<ReservationDto>> getReservations(PrincipalDetails principal) {
        if (principal == null)
            return ResponseEntity.badRequest().build();

        List<Reservation> reservations = reservationRepository.findAllByAccountEmailOrderByCreatedAtDesc(principal.getUsername());
        List<ReservationDto> reservationDtos = reservations.stream()
                .map(reservation -> new ReservationDto(
                        reservation.getId(),
                        reservation.getCounselor(),
                        reservation.getCounselDate(),
                        reservation.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(reservationDtos);
    }

}


