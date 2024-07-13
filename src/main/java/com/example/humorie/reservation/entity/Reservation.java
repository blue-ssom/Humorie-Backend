package com.example.humorie.reservation.entity;

import com.example.humorie.account.entity.AccountDetail;
import com.example.humorie.consultant.counselor.entity.Counselor;
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

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountDetail account;

    @OneToOne
    @JoinColumn(name = "counselor_id")
    private Counselor counselor;

    private String location;

    private LocalDate counselDate;

    private LocalTime counselTime;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public Reservation(AccountDetail account, Counselor counselor,String location, LocalDate counselDate, LocalTime counselTime) {
        this.account = account;
        this.counselor = counselor;
        this.location = location;
        this.counselDate = counselDate;
        this.counselTime = counselTime;
        this.createdAt = LocalDateTime.now();
    }


}
