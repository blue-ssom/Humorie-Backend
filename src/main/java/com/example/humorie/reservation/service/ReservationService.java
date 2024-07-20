package com.example.humorie.reservation.service;

import com.example.humorie.account.jwt.PrincipalDetails;
import com.example.humorie.consultant.counselor.entity.Counselor;
import com.example.humorie.consultant.counselor.repository.CounselorRepository;
import com.example.humorie.payment.entity.Payment;
import com.example.humorie.payment.entity.PaymentStatus;
import com.example.humorie.payment.repository.PaymentRepository;
import com.example.humorie.reservation.dto.ReservationDto;
import com.example.humorie.reservation.dto.request.CreateReservationReq;
import com.example.humorie.reservation.dto.response.AvailableReservationDatesResDto;
import com.example.humorie.reservation.dto.response.AvailableReservationTimesResDto;
import com.example.humorie.reservation.dto.response.CreateReservationResDto;
import com.example.humorie.reservation.dto.response.GetReservationResDto;
import com.example.humorie.reservation.entity.Reservation;
import com.example.humorie.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CounselorRepository counselorRepository;
    private final PaymentRepository paymentRepository;

    private final static int MAX_DAILY_RESERVATIONS = 10;
    private final static int MAX_RESERVATION_DATE = 14; // 2주
    private final static int RESERVATION_START_TIME = 10;
    private final static int RESERVATION_END_TIME = 19;

    public ResponseEntity<CreateReservationResDto> createReservation(PrincipalDetails principal, CreateReservationReq createReservationReq) {
        Counselor counselor = counselorRepository.findById(createReservationReq.getCounselorId())
                .orElseThrow(() -> new RuntimeException("Counselor Not Found"));

        if(principal == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 임시 결제내역 생성
        Payment payment = Payment.builder()
                .price(createReservationReq.getPrice())
                .status(PaymentStatus.READY)
                .build();

        paymentRepository.save(payment);

        Reservation reservation = Reservation.builder()
                .account(principal.getAccountDetail())
                .counselor(counselor)
                .payment(payment)
                .reservationUid(UUID.randomUUID().toString())
                .counselContent(createReservationReq.getCounselContent())
                .counselDate(createReservationReq.getCounselDate())
                .counselTime(createReservationReq.getCounselTime())
                .location(createReservationReq.getLocation())
                .build();

        reservationRepository.save(reservation);
        return ResponseEntity.ok(new CreateReservationResDto(reservation.getReservationUid()));
    }

    public ResponseEntity<List<ReservationDto>> getReservations(PrincipalDetails principal) {
        if (principal == null)
            return ResponseEntity.badRequest().build();

        List<Reservation> reservations = reservationRepository.findAllByAccountEmailOrderByCreatedAtDesc(principal.getUsername());
        List<ReservationDto> reservationDtos = reservations.stream()
                .map(reservation -> new ReservationDto(
                        reservation.getId(),
                        reservation.getCounselor().getName(),
                        reservation.getLocation(),
                        reservation.getCounselDate(),
                        reservation.getCounselTime(),
                        reservation.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(reservationDtos);
    }

    public ResponseEntity<GetReservationResDto> getReservation(String uid){

        Reservation reservation = reservationRepository.findReservationByReservationUid(uid)
                .orElseThrow(() -> new RuntimeException("Reservation Not Found"));

        GetReservationResDto resDto = GetReservationResDto.builder()
                .ReservationUid(reservation.getReservationUid())
                .buyerEmail(reservation.getAccount().getEmail())
                .buyerName(reservation.getAccount().getName())
                .counselorName(reservation.getCounselor().getName())
                .build();

        return ResponseEntity.ok(resDto);
    }

    public ResponseEntity<AvailableReservationDatesResDto> getAvailableReservationDate(Long counselorId){
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        for (int i=0; i<MAX_RESERVATION_DATE; i++){
            dateList.add(currentDate.plusDays(i));
        }

        for (int i=0; i<dateList.size(); i++){
            LocalDate checkDate = dateList.get(i);
            int countReservation = reservationRepository.countByCounselorIdAndCounselDate(counselorId, checkDate);

            if(countReservation >= MAX_DAILY_RESERVATIONS ||
                    (checkDate.isEqual(currentDate) && currentTime.isAfter(LocalTime.of(RESERVATION_END_TIME,0)))){

                dateList.remove(i);
                i--;
            }
        }

        return ResponseEntity.ok(new AvailableReservationDatesResDto(dateList));
    }

    public ResponseEntity<AvailableReservationTimesResDto> getAvailableReservationTime(Long counselorId, LocalDate selectDate){
        List<LocalTime> timeList = new ArrayList<>();
        LocalTime currentTime = LocalTime.now();
        LocalDate currentDate = LocalDate.now();
        for (int hour = RESERVATION_START_TIME; hour <= RESERVATION_END_TIME; hour++) {
            timeList.add(LocalTime.of(hour, 0));
        }

        for (int i=0; i<timeList.size(); i++){
            LocalTime checkTime = timeList.get(i);
            boolean existReservation = reservationRepository.existsByCounselorIdAndCounselDateAndCounselTime(counselorId, selectDate, checkTime);
            if(existReservation || (selectDate.isEqual(currentDate)  && currentTime.isAfter(checkTime))){
                timeList.remove(i);
                i--;
            }
        }

        return ResponseEntity.ok(new AvailableReservationTimesResDto(timeList));
    }

}


