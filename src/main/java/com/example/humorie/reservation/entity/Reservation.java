package com.example.humorie.reservation.entity;

import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.consultant.counselor.entity.Counselor;
import com.example.humorie.payment.entity.Payment;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String reservationUid; // 예약 번호

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountDetail account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "counselor_id")
    private Counselor counselor;

    private String counselContent;

    private String location;

    private LocalDate counselDate;

    private LocalTime counselTime;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public Reservation(AccountDetail account, Counselor counselor, Payment payment, String reservationUid, String location,
                       String counselContent, LocalDate counselDate, LocalTime counselTime) {
        this.account = account;
        this.counselor = counselor;
        this.payment = payment;
        this.reservationUid = reservationUid;
        this.location = location;
        this.counselContent = counselContent;
        this.counselDate = counselDate;
        this.counselTime = counselTime;
        this.createdAt = LocalDateTime.now();
    }


}
